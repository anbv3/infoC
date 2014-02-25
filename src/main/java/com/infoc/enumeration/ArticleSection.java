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
	IT,
	OTHERS;

	public static ArticleSection find(String type) {
		for (ArticleSection each : ArticleSection.values()) {
			if (each.toString().equalsIgnoreCase(type)) {
				return each;
			}
		}
		throw new IllegalArgumentException("Not Found Type " + type);
	}
}
