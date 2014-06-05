package com.infoc.util;

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
			LOG.debug("{}", EconInfoCrawler.getCurrency());
		} catch (Exception e) {
		}
	}
	
	
	@Test
	public void testGetStock() {
		
		try {
			LOG.debug("{}", EconInfoCrawler.getStock());
			
		} catch (Exception e) {
		}
	}
	

	@Test
	public void testGetStockChange() {
		
		try {
			String a = "4.04 -0.79%상승";
			
			LOG.debug("{}", a.replaceAll(".*[ ]", "").replaceAll("%.*", ""));
		} catch (Exception e) {
		}
	}
}
