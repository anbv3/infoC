/*
 * @(#)HTTPHandler.java $version 2013. 10. 3.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.util;

import java.util.HashMap;
import java.util.Map;

import org.jdom.input.SAXBuilder;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author NBP
 */
public class EconInfoCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(EconInfoCrawler.class);

	private static final String USD_CURRENCY = "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=USD&ToCurrency=KRW";
	private static final String CNY_CURRENCY = "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=CNY&ToCurrency=KRW";

	public static String getUSD() throws Exception {
		org.jdom.Document doc = new SAXBuilder().build(USD_CURRENCY);
		org.jdom.Element root = doc.getRootElement();
		return root.getText();
	}
	
	public static String getCNY() throws Exception {
		org.jdom.Document doc = new SAXBuilder().build(CNY_CURRENCY);
		org.jdom.Element root = doc.getRootElement();
		return root.getText();
	}
	
	
	public static Map<String, String> getStock() throws Exception {
		org.jsoup.nodes.Document doc = Jsoup.connect("http://finance.naver.com/sise").get();
		org.jsoup.select.Elements kospi = doc.select("#KOSPI_now");
		org.jsoup.select.Elements kospi_change = doc.select("#KOSPI_change");
		org.jsoup.select.Elements kosdaq = doc.select("#KOSDAQ_now");
		org.jsoup.select.Elements kosdaq_change = doc.select("#KOSDAQ_change");
		
		Map<String, String> stockInfo = new HashMap<>();
		stockInfo.put("kospi", kospi.text());
		stockInfo.put("kospi_change", kospi_change.html());
		stockInfo.put("kosdaq", kosdaq.text());
		stockInfo.put("kosdaq_change", kosdaq_change.html());
		return stockInfo;
	}
	
/* sample code for later
	public void httpGet() {
		HttpGet request = new HttpGet(USD_CURRENCY);

		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse response = httpClient.execute(request);
			LOG.debug("{}", response.getStatusLine());

			HttpEntity resEntity = response.getEntity();
			if (response.getEntity() == null) {
			}

			String xml = EntityUtils.toString(resEntity);
			LOG.debug("{}", xml);
			EntityUtils.consume(resEntity);
			
		} catch (Exception e) {
			LOG.error("", e);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
*/
	
}
