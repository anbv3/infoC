/*
 * @(#)HTTPHandlerTest.java $version 2013. 10. 3.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author NBP
 */
public class EconInfoCrawlerTest {
	private static final Logger LOG = LoggerFactory.getLogger(EconInfoCrawlerTest.class);
	
	@Test
	public void testGetUSD() {
		
		try {
			LOG.debug("{}", EconInfoCrawler.getUSD());
			LOG.debug("{}", EconInfoCrawler.getCNY());
		} catch (Exception e) {
		}
	}

	
	@Test
	public void testGetStock() {
		
		try {
			Document doc = Jsoup.connect("http://finance.naver.com/sise").get();
			Elements kospi = doc.select("#KOSPI_now");
			Elements kospi_change = doc.select("#KOSPI_change");
			Elements kosdaq = doc.select("#KOSDAQ_now");
			Elements kosdaq_change = doc.select("#KOSDAQ_change");
			
			LOG.debug("{}", kospi.text());
			LOG.debug("{}", kospi_change.html());
			LOG.debug("{}", kosdaq.text());
			LOG.debug("{}", kosdaq_change.html());
		} catch (Exception e) {
		}
	}
}
