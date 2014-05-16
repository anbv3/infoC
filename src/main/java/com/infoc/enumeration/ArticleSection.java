package com.infoc.enumeration;


import java.util.List;
import java.util.Map;

import com.infoc.domain.Article;
import com.infoc.service.CollectionService;
import com.infoc.service.USCollectionService;

/**
 *	section type of the news articles
 */
public enum ArticleSection {

	TODAY("main", CollectionService.TODAY_CACHE, USCollectionService.TODAY_CACHE),
	POLITICS("politics", CollectionService.POLITICS_CACHE, USCollectionService.POLITICS_CACHE),
	ECON("econ", CollectionService.ECON_CACHE, USCollectionService.ECON_CACHE),
	SOCIETY("society", CollectionService.SOCIETY_CACHE, USCollectionService.SOCIETY_CACHE),
	CULTURE("culture", CollectionService.CULTURE_CACHE, USCollectionService.CULTURE_CACHE),
	ENT("ent", CollectionService.ENT_CACHE, USCollectionService.ENT_CACHE),
	SPORT("sport", CollectionService.SPORT_CACHE, USCollectionService.SPORT_CACHE),
	IT("it", CollectionService.IT_CACHE, USCollectionService.IT_CACHE),
	OTHERS("others", CollectionService.OTHERS_CACHE, USCollectionService.OTHERS_CACHE);

	private String section;
    private Map<Integer, List<Article>> krCache;
    private Map<Integer, List<Article>> usCache;
	
	ArticleSection(String section, Map<Integer, List<Article>> krCache, Map<Integer, List<Article>> usCache) {
		this.section = section;
        this.krCache = krCache;
        this.usCache = usCache;
	}
	
	public static ArticleSection find(String type) {
		for (ArticleSection each : ArticleSection.values()) {
			if (each.getSection().equalsIgnoreCase(type)) {
				return each;
			}
		}
		throw new IllegalArgumentException("Not Found Type " + type);
	}

    public static Map<Integer, List<Article>> findKRCache(String type) {
        for (ArticleSection each : ArticleSection.values()) {
            if (each.getSection().equalsIgnoreCase(type)) {
                return each.getKrCache();
            }
        }
        throw new IllegalArgumentException("Not Found Type " + type);
    }
    
    public static Map<Integer, List<Article>> findUSCache(String type) {
    	for (ArticleSection each : ArticleSection.values()) {
    		if (each.getSection().equalsIgnoreCase(type)) {
    			return each.getUsCache();
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

    public void setKrCache(Map<Integer, List<Article>> krCache) {
		this.krCache = krCache;
	}

	public Map<Integer, List<Article>> getKrCache() {
        return krCache;
    }

	public Map<Integer, List<Article>> getUsCache() {
		return usCache;
	}

	public void setUsCache(Map<Integer, List<Article>> usCache) {
		this.usCache = usCache;
	}
}
