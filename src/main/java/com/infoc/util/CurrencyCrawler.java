/*
 * @(#)HTTPHandler.java $version 2013. 10. 3.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.util;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author NBP
 */
public class CurrencyCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(CurrencyCrawler.class);

	private static final String USD_CURRENCY = "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=USD&ToCurrency=KRW";
	private static final String CNY_CURRENCY = "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency=CNY&ToCurrency=KRW";

	public static String getUSD() throws Exception {
		Document doc = new SAXBuilder().build(USD_CURRENCY);
		Element root = doc.getRootElement();
		return root.getText();
	}
	
	public static String getCNY() throws Exception {
		Document doc = new SAXBuilder().build(CNY_CURRENCY);
		Element root = doc.getRootElement();
		return root.getText();
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
