package com.timshadel.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class StackTraceUtil {
	private static final String thisClassPattern = "^" + StackTraceUtil.class.getCanonicalName() + "[^a-zA-Z].*";

	public List<String> getTrace() {
		ArrayList<String> traceElements = new ArrayList<String>();
		StackTraceElement[] elements;
		try {
			throw new Exception();
		} catch (Exception e) {
			elements = e.getStackTrace();
		}
		for(StackTraceElement e : elements) {
			traceElements.add(e.toString());
		}
		return traceElements;
	}

	public List<String> getTrace(String pattern) {
		List<String> list = getTrace();
		List<String> matchingList = new ArrayList<String>();
		for(String s : list) {
			if (s.matches(pattern) && !s.matches(thisClassPattern)) {
				matchingList.add(s);
			}
		}
		return matchingList;
	}

}
