/*
 * @(#)HTTPHandlerTest.java $version 2013. 10. 3.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author NBP
 */
public class CurrencyCrawlerTest {
	private static final Logger LOG = LoggerFactory.getLogger(CurrencyCrawlerTest.class);
	
	@Test
	public void testGetUSD() {
		
		try {
			LOG.debug("{}", CurrencyCrawler.getUSD());
			LOG.debug("{}", CurrencyCrawler.getCNY());
		} catch (Exception e) {
		}
	}

}
