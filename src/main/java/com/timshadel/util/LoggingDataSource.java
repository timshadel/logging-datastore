package com.timshadel.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DelegatingDataSource;

public class LoggingDataSource extends DelegatingDataSource {

	private Logger log = Logger.getLogger(LoggingDataSource.class);
	private String filterPattern;
	private String indent;

	public LoggingDataSource() {
		setIndentSize(8);
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		Connection connection;
		try {
			connection = super.getConnection();
			logConnectionRequest();
		} catch (RuntimeException e) {
			logConnectionRequest(e);
			throw e;
		} catch (SQLException e) {
			logConnectionRequest(e);
			throw e;
		}
		return connection;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		Connection connection;
		try {
			connection = super.getConnection(username, password);
			logConnectionRequest();
		} catch (RuntimeException e) {
			logConnectionRequest(e);
			throw e;
		} catch (SQLException e) {
			logConnectionRequest(e);
			throw e;
		}
		return connection;
	}

	private void logConnectionRequest() {
		logConnectionRequest(null);
	}

	private void logConnectionRequest(Throwable exceptionThrown) {
		if (filterPattern == null) {
			log.debug("No filter set.  Not pulling stacktrace.");
			return;
		}
		StackTraceUtil util = new StackTraceUtil();
		List<String> trace = util.getTrace(filterPattern);
		if (trace.size() == 0) {
			log.debug("No trace entries.");
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Connection request stack trace, filtered by '" + filterPattern + "'\n");
		if (exceptionThrown != null) {
			sb.append(indent);
			sb.append("CAUGHT EXCEPTION (");
			sb.append(exceptionThrown.getClass().getName());
			sb.append("): ");
			sb.append(exceptionThrown.getMessage());
			sb.append("\n");
		}
		for(String s : trace) {
			if (!s.matches("^com.timshadel.util.LoggingDataSource[^a-zA-Z].*")) {
				sb.append(indent);
				sb.append(s);
				sb.append("\n");
			}
		}
		if (exceptionThrown == null) {
			log.info(sb.toString());
		} else {
			log.error(sb.toString());
		}
	}

	public void setFilterPattern(String pattern) {
		this.filterPattern = pattern;
	}

	public void setIndent(String indent) {
		this.indent = indent;
	}

	public void setIndentSize(int size) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < size; i++) {
			sb.append(" ");
		}
		this.indent = sb.toString();
	}
}
