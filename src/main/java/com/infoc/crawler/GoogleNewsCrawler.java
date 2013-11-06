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

public class GoogleNewsCrawler implements NewsCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(GoogleNewsCrawler.class);
	private static String RSS_URL = "https://news.google.co.kr/nwshp?hl=ko&output=rss"; // 주요기사

	@Override
	public List<Article> createArticlList() {
		List<Article> articleList = new ArrayList<>();
		List<SyndEntry> rssList = RSSCrawler.getArticleList(RSS_URL);

		LOG.debug("GoogleNews size: {}", rssList.size());

		for (SyndEntry item : rssList) {
			articleList.add(parseRSSItem(item));
		}

		return articleList;
	}

	private Article parseRSSItem(SyndEntry rssItem) {
		Article article = new Article();
		article.setLink(rssItem.getLink());
		article.setPubDate(new DateTime(rssItem.getPublishedDate(), DateTimeZone.forID("Asia/Seoul")));
		parseTitleAuthor(rssItem.getTitle(), article);
		
		article.createContentsFromLink();
		if (Strings.isNullOrEmpty(article.getContents())) {
			article.setContents(rssItem.getDescription().getValue());
		}
		
		ContentsAnalysisService.clearInvalidWords(article);
		
		return article;
	}

	private void parseTitleAuthor(String title, Article article) {
		int idx = title.lastIndexOf("-");
		article.setTitle(title.substring(0, idx).trim());
		article.setAuthor(title.substring(idx + 1, title.length()).trim());
	}
}
