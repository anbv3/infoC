package com.infoc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MorphemeAnalyzer {
	private static final Logger LOG = LoggerFactory
			.getLogger(MorphemeAnalyzer.class);
	private static MorphemeAnalyzer ourInstance = new MorphemeAnalyzer();

	public static MorphemeAnalyzer getInstance() {
		return ourInstance;
	}

	private MorphemeAnalyzer() {
	}

}
