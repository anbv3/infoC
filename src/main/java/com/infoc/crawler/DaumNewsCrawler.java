/*
 * @(#)DaumNewsCrawler.java $version 2013. 10. 25.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.crawler;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infoc.domain.Article;
import com.infoc.util.RSSCrawler;
import com.sun.syndication.feed.synd.SyndEntry;

/**
 * @author NBP
 */
public class DaumNewsCrawler implements NewsCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(DaumNewsCrawler.class);
	private static String RSS_URL = "http://media.daum.net/syndication/today_sisa.rss"; // 주요기사

	@Override
	public List<Article> createArticlList() {
		List<Article> articleList = new ArrayList<>();
		List<SyndEntry> rssList = RSSCrawler.getArticleList(RSS_URL);

		LOG.debug("D News size: {}", rssList.size());

		for (SyndEntry item : rssList) {
			articleList.add(parseRSSItem(item));
		}

		return articleList;
	}

	private Article parseRSSItem(SyndEntry rssItem) {
		Article article = new Article();
		article.setAuthor(rssItem.getAuthor());
		article.setLink(rssItem.getLink());
		article.setPubDate(new DateTime(rssItem.getPublishedDate()));
		article.setTitle(rssItem.getTitle());

		article.createContentsFromLink();
		clearInvalidWords(article);

		return article;
	}

	/**
	 * extract some text enclosed by special characters
	 * each article has different extraction rules. 
	 */
	public void clearInvalidWords(Article article) {
		String desc = article.getContents();

		StringBuffer sb = new StringBuffer();
		boolean appendFlag = true;
		for (int i = 0; i < desc.length(); i++) {
			char c = desc.charAt(i);
			if (c == '[' || c == '【' || c == '(') {
				appendFlag = false;
				continue;
			}
			if (c == ']' || c == '】' || c == ')') {
				appendFlag = true;
				continue;
			}

			if (appendFlag) {
				sb.append(c);
			}
		}

		article.setContents(sb.toString());
	}
}
