/*
 * @(#)Nnews.java $version 2013. 7. 13.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.rss;

import java.util.ArrayList;
import java.util.List;

import com.infoc.domain.Article;
import com.infoc.util.RSSReader;
import com.sun.syndication.feed.synd.SyndEntry;

/**
 * @author anbv3
 */
public class Nnews {

	private static String N_NEWS = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=2";
	
	public List<Article> getNews() {
		List<SyndEntry> rssList = RSSReader.getArticleList(N_NEWS);
		
		List<Article> articleList = new ArrayList<>();
		for(SyndEntry item : rssList) {
			articleList.add(parseItem(item));
		}
		
		return articleList;
	}
	
	public Article parseItem(SyndEntry rssItem) {
		Article article = new Article();
		article.setAuthor(rssItem.getAuthor());
		article.setContents(rssItem.getDescription().getValue());
		article.setLink(rssItem.getLink());
		article.setPubDate(rssItem.getPublishedDate());
		article.setTitle(rssItem.getTitle());
		return article;
	}
	
}
