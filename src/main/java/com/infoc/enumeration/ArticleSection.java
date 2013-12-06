/*
 * @(#)ArticleSection.java $version 2013. 12. 6.
 *
 */

package com.infoc.enumeration;


/**
 *	section type of the news articles
 */
public enum ArticleSection {

	TODAY,
	POLITICS,
	ECON,
	SOCIETY,
	CULTURE,
	ENT,
	SPORT,
	IT_URL;

	public static ArticleSection find(String type) {
		for (ArticleSection each : ArticleSection.values()) {
			if (each.toString().equals(type)) {
				return each;
			}
		}
		throw new IllegalArgumentException("Not Found Type " + type);
	}
}
