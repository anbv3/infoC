package com.infoc.enumeration;


/**
 *	section type of the news articles
 */
public enum ArticleSection {

	TODAY("main"),
	POLITICS("politics"),
	ECON("econ"),
	SOCIETY("society"),
	CULTURE("culture"),
	ENT("ent"),
	SPORT("sport"),
	IT("it"),
	OTHERS("others");

	private String section;
	
	ArticleSection(String section) {
		this.section = section;
	}
	
	public static ArticleSection find(String type) {
		for (ArticleSection each : ArticleSection.values()) {
			if (each.getSection().equalsIgnoreCase(type)) {
				return each;
			}
		}
		throw new IllegalArgumentException("Not Found Type " + type);
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
	
}
