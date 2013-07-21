/*
 * @(#)Nnews.java $version 2013. 7. 13.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.rss;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infoc.domain.Article;
import com.infoc.domain.Collector;
import com.infoc.util.RSSReader;
import com.sun.syndication.feed.synd.SyndEntry;

/**
 * @author anbv3
 */
public class Nnews {
	private static final Logger LOG = LoggerFactory.getLogger(Nnews.class);
	
	private static String N_NEWS = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=2";
	
	public Nnews getNews() {
		List<SyndEntry> rssList = RSSReader.getArticleList(N_NEWS);
		LOG.debug("news size: {}", rssList.size());
		
		for(SyndEntry item : rssList) {
		
			Article article = parseItem(item);
			int hour = article.getPubDate().getHourOfDay();
			Collector.CACHE.get(hour).add(article);
			
		}
		
		return this;
	}
	
	public Article parseItem(SyndEntry rssItem) {
		Article article = new Article();
		article.setAuthor(rssItem.getAuthor());
		article.setContents(rssItem.getDescription().getValue());
		article.setLink(rssItem.getLink());
		article.setPubDate(new DateTime(rssItem.getPublishedDate()));
		article.setTitle(rssItem.getTitle());
		return article;
	}
	
}
