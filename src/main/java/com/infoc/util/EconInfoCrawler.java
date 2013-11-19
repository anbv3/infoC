/*
 * @(#)HTTPHandler.java $version 2013. 10. 3.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.util;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;

/**
 * @author NBP
 */
public class EconInfoCrawler {
	public static Map<String, String> getCurrency() throws Exception {
		org.jsoup.nodes.Document doc = Jsoup.connect("http://info.finance.naver.com/marketindex/").get();
		org.jsoup.select.Elements usd = doc.select(".usd > .head_info > .value");
		org.jsoup.select.Elements cny = doc.select(".cny > .head_info > .value");
		
		Map<String, String> currencyInfo = new HashMap<>();
		currencyInfo.put("usd", usd.text());
		currencyInfo.put("cny", cny.text());
		
		return currencyInfo;
	}

	public static Map<String, String> getStock() throws Exception {
		org.jsoup.nodes.Document doc = Jsoup.connect("http://finance.naver.com/sise").get();
		org.jsoup.select.Elements kospi = doc.select("#KOSPI_now");
		org.jsoup.select.Elements kospi_change = doc.select("#KOSPI_change");
		org.jsoup.select.Elements kosdaq = doc.select("#KOSDAQ_now");
		org.jsoup.select.Elements kosdaq_change = doc.select("#KOSDAQ_change");

		Map<String, String> stockInfo = new HashMap<>();
		stockInfo.put("kospi", kospi.text());
		stockInfo.put("kospiChange", kospi_change.text().replaceAll(".*[ ]", "").replaceAll("%.*", "") + "%");

		stockInfo.put("kosdaq", kosdaq.text());
		stockInfo.put("kosdaqChange", kosdaq_change.text().replaceAll(".*[ ]", "").replaceAll("%.*", "") + "%");
		return stockInfo;
	}
}
