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
import com.infoc.util.RSSCrawler;
import com.sun.syndication.feed.synd.SyndEntry;

/**
 * @author NBP
 */
public class NaverNewsCrawler implements NewsCrawler {
	private static final Logger LOG = LoggerFactory
			.getLogger(NaverNewsCrawler.class);
	private static String RSS_URL = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=2";

	@Override
	public List<Article> createArticlList() {
		List<Article> articleList = new ArrayList<>();
		List<SyndEntry> rssList = RSSCrawler.getArticleList(RSS_URL);

		LOG.debug("Naver News size: {}", rssList.size());

		for (SyndEntry item : rssList) {
			Article article = parseRSSItem(item);
			if (article == null) {
				continue;
			}

			articleList.add(article);
		}

		return articleList;
	}

	private Article parseRSSItem(SyndEntry rssItem) {
		// Skip the news written in only English.
		if (!rssItem.getTitle().matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
			return null;
		}

		Article article = new Article();
		article.setAuthor(rssItem.getAuthor());
		article.setLink(rssItem.getLink());
		article.setPubDate(new DateTime(rssItem.getPublishedDate(),
				DateTimeZone.forID("Asia/Seoul")));
		article.setTitle(rssItem.getTitle());
		
		article.createContentsFromLink();
		if (Strings.isNullOrEmpty(article.getContents())) {
			article.setContents(rssItem.getDescription().getValue());
		}

		clearInvalidWords(article);

		return article;
	}

	/**
	 * extract some text enclosed by special characters each article has
	 * different extraction rules.
	 */
	public void clearInvalidWords(Article article) {
		String desc = article.getContents().replaceAll("&nbsp;", "");

		StringBuffer sb = new StringBuffer();
		boolean appendFlag = true;
		for (int i = 0; i < desc.length(); i++) {
			char c = desc.charAt(i);
			if (c == '【') {
				appendFlag = false;
				continue;
			}
			if (c == '】') {
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
