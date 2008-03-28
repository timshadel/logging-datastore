package com.timshadel.util;
import java.util.List;

import junit.framework.TestCase;


public class StackTraceUtilTest extends TestCase {

	public void testShouldReturnListOfStrings() {
		StackTraceUtil u = new StackTraceUtil();
		List<String> list = u.getTrace();
		assertNotNull("List should not be null.", list);
		assertTrue("List should contain something.", list.size() > 0);
	}

	public void testShouldOnlyContainMatchingStrings() {
		StackTraceUtil u = new StackTraceUtil();
		String pattern = "^com.timshadel.*";
		List<String> list = u.getTrace(pattern);
		assertNotNull("List should not be null.", list);
		for(String s : list) {
			assertTrue("Should match '" + pattern + "'. '" + s + "'", s.matches(pattern));
		}
		assertEquals(1, list.size());
	}

	public void testShouldOnlyContainMatchingStringsForEither() {
		StackTraceUtil u = new StackTraceUtil();
		String pattern = "^(com.timshadel|junit).*";
		List<String> list = u.getTrace(pattern);
		assertNotNull("List should not be null.", list);
		for(String s : list) {
			assertTrue("Should match '" + pattern + "'. '" + s + "'", s.matches(pattern));
		}
		assertEquals(9, list.size());
	}

	public void testShouldNotContainSelfReferences() {
		StackTraceUtil u = new StackTraceUtil();
		String pattern = "^(com.timshadel|junit).*";
		List<String> list = u.getTrace(pattern);
		assertNotNull("List should not be null.", list);
		String badPattern = "^com.timshadel.util.StackTraceUtil[^a-zA-Z].*";
		for(String s : list) {
			assertFalse("Should not match '" + badPattern + "'. '" + s + "'", s.matches(badPattern));
		}
		assertEquals(9, list.size());
	}

}