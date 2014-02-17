/*
 * @(#)TranslationParser.java $version 2014. 2. 9.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author NBP
 */
public class TranslationParser {
	private static final Logger LOG = LoggerFactory.getLogger(TranslationParser.class);

	/**
	 * 1. url encode the input string(kr)
	 * 2. send the url request of google translation with that by a little hack.
	 * 3. parse, gather, and return.
	 * 
	 */
	public static String krToEn(String kr) {

		Document doc;
		try {
			String q = "http://translate.google.com/translate_a/t?client=t&sl=ko&tl=en&hl=ko&ie=UTF-8&oe=UTF-8&prev=btn&ssel=0&tsel=0";

			String reqStr = kr.replaceAll("“", "'");
			
			doc = Jsoup.connect(q).userAgent("Mozilla").data("q", reqStr).post();

			String ans = doc.text();
			// LOG.debug("{}", ans);

			int idx = ans.indexOf("]]");
			String tr = ans.substring(2, idx + 2);
			String[] arr = tr.split("],");

			StringBuilder sb = new StringBuilder();
			for (String sen : arr) {
//				LOG.debug("{}", sen);
				String[] arr2 = sen.split("\",\"");
				String s = arr2[0];

				// TODO: make it better... :-(
				String f = s.substring(2)
					.replaceAll(" \\.", ".")
					.replaceAll("\\\\\"", "\"")
					.replaceAll(" ,", ",")
					.replaceAll(" 's", "'s")
					.replaceAll(" ' ", " '");

//				LOG.debug("{}", f);
				sb.append(f);
			}

			return sb.toString();

		} catch (Exception e) {
			LOG.error("{}\n", kr, e);
			return "";
		}

	}
	
	public static String enToKr(String en) {

		Document doc;
		try {
			String q = "http://translate.google.com/translate_a/t?client=t&sl=en&tl=ko&hl=ko&ie=UTF-8&oe=UTF-8&prev=btn&ssel=3&tsel=6";

			String reqStr = en.replaceAll("“", "'");
			
			doc = Jsoup.connect(q).userAgent("Mozilla").data("q", reqStr).post();

			String ans = doc.text();

			int idx = ans.indexOf("]]");
			String tr = ans.substring(2, idx + 2);
			String[] arr = tr.split("],");

			StringBuilder sb = new StringBuilder();
			for (String sen : arr) {
//				LOG.debug("{}", sen);
				String[] arr2 = sen.split("\",\"");
				String s = arr2[0];

				// TODO: make it better... :-(
				String f = s.substring(2)
					.replaceAll(" \\.", ".")
					.replaceAll("\\\\\"", "\"")
					.replaceAll(" ,", ",")
					.replaceAll(" 's", "'s")
					.replaceAll(" ' ", " '");

//				LOG.debug("{}", f);
				sb.append(f);
			}

			return sb.toString();

		} catch (Exception e) {
			LOG.error("{}\n", en, e);
			return "";
		}

	}
}
