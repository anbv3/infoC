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
import com.infoc.enumeration.ArticleSection;
import com.infoc.service.ContentsAnalysisService;
import com.infoc.util.RSSCrawler;
import com.sun.syndication.feed.synd.SyndEntry;

public class DaumNewsCrawler implements NewsCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(DaumNewsCrawler.class);
	
	private static String TODAY_URL = "http://media.daum.net/syndication/today_sisa.rss"; 
	private static String POLITICS_URL = "http://media.daum.net/rss/part/primary/politics/rss2.xml"; 
	private static String ECON_URL = "http://media.daum.net/rss/part/primary/economic/rss2.xml"; 
	private static String SOCIETY_URL = "http://media.daum.net/rss/part/primary/society/rss2.xml"; 
	private static String CULTURE_URL = "http://media.daum.net/rss/part/primary/culture/rss2.xml"; 
	private static String ENT_URL = "http://media.daum.net/rss/part/primary/entertain/rss2.xml"; 
	private static String SPORT_URL = "http://media.daum.net/rss/today/primary/sports/rss2.xml"; 
	private static String IT_URL = "http://media.daum.net/rss/part/primary/digital/rss2.xml"; 

	@Override
	public List<Article> createArticlList() {
		LOG.debug("get RSS from Daum.");
		
		List<Article> articleList = new ArrayList<>();
		
		for (SyndEntry item : RSSCrawler.getArticleList(TODAY_URL)) {
			Article article = parseRSSItem(item, ArticleSection.TODAY);
			if (article == null) {
				continue;
			}
			
			articleList.add(article);
		}
		for (SyndEntry item : RSSCrawler.getArticleList(POLITICS_URL)) {
			Article article = parseRSSItem(item, ArticleSection.POLITICS);
			if (article == null) {
				continue;
			}
			
			articleList.add(article);
		}

		return articleList;
	}

	private Article parseRSSItem(SyndEntry rssItem, ArticleSection section) {
		Article article = new Article();
		article.setSection(section);
		article.setAuthor(rssItem.getAuthor());
		article.setLink(rssItem.getLink());
		article.setPubDate(new DateTime(rssItem.getPublishedDate(), DateTimeZone.forID("Asia/Seoul")));
		article.setTitle(ContentsAnalysisService.clearInvalidWords(rssItem.getTitle()));
		if (Strings.isNullOrEmpty(article.getTitle())) {
			return null;
		}
		
		article.createContentsFromLink();
		if (Strings.isNullOrEmpty(article.getContents())) {
			article.setContents(rssItem.getDescription().getValue());
		}
		
		article.setContents(ContentsAnalysisService.clearInvalidWords(article.getContents()));
		
		return article;
	}

}
