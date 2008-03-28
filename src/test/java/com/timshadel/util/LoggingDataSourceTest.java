package com.timshadel.util;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class LoggingDataSourceTest extends MockObjectTestCase {

	public void testShouldLogConnectionRequest() throws SQLException {
		LoggingDataSource lds = new LoggingDataSource();
		Mock mockDataSource = new Mock(DataSource.class);

		lds.setTargetDataSource((DataSource) mockDataSource.proxy());
		lds.setFilterPattern("^com.timshadel.*");
		lds.setIndentSize(8);
		
		mockDataSource.expects(once()).method("getConnection");
		
		redirect(lds);
	}

	public void testShouldLogConnectionRequestAfterException() throws SQLException {
		LoggingDataSource lds = new LoggingDataSource();
		Mock mockDataSource = new Mock(DataSource.class);

		lds.setTargetDataSource((DataSource) mockDataSource.proxy());
		lds.setFilterPattern("^com.timshadel.*");
		
		mockDataSource.expects(once()).method("getConnection").will(throwException(new SQLException("Pretend there's no more connections.")));
		
		try {
			redirect(lds);
			fail("Exepected an exception.");
		} catch (SQLException sqle) {
			// Test to make sure the log4j thing includes error info.
		}
	}

	private void redirect(DataSource ds) throws SQLException {
		another(ds);
	}

	private void another(DataSource ds) throws SQLException {
		ds.getConnection();
	}
}
