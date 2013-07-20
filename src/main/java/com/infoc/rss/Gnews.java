/*
 * @(#)Gnews.java $version 2013. 7. 13.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.rss;

import java.util.List;

import org.joda.time.DateTime;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.infoc.domain.Article;
import com.infoc.domain.Collector;
import com.infoc.util.RSSReader;
import com.sun.syndication.feed.synd.SyndEntry;

/**
 * @author anbv3
 */
public class Gnews {
	
	private static String G_NEWS = "https://news.google.co.kr/nwshp?hl=ko&output=rss";

	public Gnews getNews() {
		List<SyndEntry> rssList = RSSReader.getArticleList(G_NEWS);
		
		for(SyndEntry item : rssList) {
			Article article = parseItem(item);
			int hour = article.getPubDate().getHourOfDay();
			Collector.CACHE.get(hour).add(article);
		}
		
		return this;
	}
	
	public Article parseItem(SyndEntry rssItem) {
		Article article = new Article();

		article.setPubDate(new DateTime(rssItem.getPublishedDate()));
		article.setContents(rssItem.getDescription().getValue());

		parseTitleAuthor(rssItem.getTitle(), article);
		parseLink(rssItem.getLink(), article);
		parseDescrption(rssItem.getDescription().getValue(), article);

		return article;
	}

	public void parseTitleAuthor(String title, Article article) {
		int idx = title.lastIndexOf("-");
		article.setTitle(title.substring(0, idx).trim());
		article.setAuthor(title.substring(idx + 1, title.length()).trim());
	}

	public void parseLink(String link, Article article) {
		int idx = link.lastIndexOf("http");
		article.setLink(link.substring(idx));
	}

	public void parseDescrption(String desc, Article article) {
		StringBuffer sb = new StringBuffer();

		boolean appendFlag = false;
		for (int i = 0; i < desc.length(); i++) {
			char c = desc.charAt(i);
			if (c == '<') {
				appendFlag = false;
				continue;
			}
			if (c == '>') {
				appendFlag = true;
				continue;
			}

			if (appendFlag) {
				sb.append(c);
			}
		}

		List<String> cList = Lists.newArrayList(Splitter.on(';')
				.trimResults()
				.omitEmptyStrings()
				.split(sb.toString()));

		int maxLength = 0;
		int maxIdx = 0;
		for (int i = 0 ; i<cList.size() ; i++) {
			if(maxLength < cList.get(i).length()) {
				maxLength = cList.get(i).length();
				maxIdx = i;
			}
		}

		article.setContents(cList.get(maxIdx));
	}
}
