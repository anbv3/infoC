/*
 * @(#)DaumNewsCrawler.java $version 2013. 10. 25.
 */

package com.infoc.crawler;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infoc.domain.Article;
import com.infoc.service.ContentsAnalysisService;
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

		LOG.debug("Daum News size: {}", rssList.size());

		for (SyndEntry item : rssList) {
			articleList.add(parseRSSItem(item));
		}

		return articleList;
	}

	private Article parseRSSItem(SyndEntry rssItem) {
		Article article = new Article();
		article.setAuthor(rssItem.getAuthor());
		article.setLink(rssItem.getLink());
		article.setPubDate(new DateTime(rssItem.getPublishedDate(), DateTimeZone.forID("Asia/Seoul")));
		article.setTitle(ContentsAnalysisService.clearInvalidWords(rssItem.getTitle()));

		article.createContentsFromLink();
		if (Strings.isNullOrEmpty(article.getContents())) {
			article.setContents(rssItem.getDescription().getValue());
		}
		
		article.setContents(ContentsAnalysisService.clearInvalidWords(article.getContents()));
		
		return article;
	}

}
