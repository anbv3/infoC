package com.infoc.enumeration;


import com.infoc.domain.Article;
import com.infoc.service.CollectionService;

import java.util.List;
import java.util.Map;

/**
 *	section type of the news articles
 */
public enum ArticleSection {

	TODAY("main", CollectionService.TODAY_CACHE),
	POLITICS("politics", CollectionService.POLITICS_CACHE),
	ECON("econ", CollectionService.ECON_CACHE),
	SOCIETY("society", CollectionService.SOCIETY_CACHE),
	CULTURE("culture", CollectionService.CULTURE_CACHE),
	ENT("ent", CollectionService.ENT_CACHE),
	SPORT("sport", CollectionService.SPORT_CACHE),
	IT("it", CollectionService.IT_CACHE),
	OTHERS("others", CollectionService.OTHERS_CACHE);

	private String section;
    private Map<Integer, List<Article>> cache;
	
	ArticleSection(String section, Map<Integer, List<Article>> cache) {
		this.section = section;
        this.cache = cache;
	}
	
	public static ArticleSection find(String type) {
		for (ArticleSection each : ArticleSection.values()) {
			if (each.getSection().equalsIgnoreCase(type)) {
				return each;
			}
		}
		throw new IllegalArgumentException("Not Found Type " + type);
	}

    public static Map<Integer, List<Article>> findCache(String type) {
        for (ArticleSection each : ArticleSection.values()) {
            if (each.getSection().equalsIgnoreCase(type)) {
                return each.getCache();
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

    public Map<Integer, List<Article>> getCache() {
        return cache;
    }

    public void setCache(Map<Integer, List<Article>> cache) {
        this.cache = cache;
    }
}
