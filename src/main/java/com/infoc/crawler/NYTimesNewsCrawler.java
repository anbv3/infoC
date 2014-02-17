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
import com.infoc.service.USCollectionService;
import com.infoc.service.USContentsAnalysisService;
import com.infoc.util.RSSCrawler;
import com.sun.syndication.feed.synd.SyndEntry;


public class NYTimesNewsCrawler implements NewsCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(NYTimesNewsCrawler.class);
	private static String TODAY = "http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml";
	private static String POLITICS = "http://rss.nytimes.com/services/xml/rss/nyt/Politics.xml";
	private static String ECON = "http://rss.nytimes.com/services/xml/rss/nyt/Economy.xml";
	private static String SOCIETY = "http://rss.nytimes.com/services/xml/rss/nyt/NYRegion.xml";
	private static String CULTURE = "http://rss.nytimes.com/services/xml/rss/nyt/FashionandStyle.xml";
	private static String ENT = "http://rss.nytimes.com/services/xml/rss/nyt/Arts.xml";
	private static String SPORT = "http://rss.nytimes.com/services/xml/rss/nyt/Sports.xml";
	private static String IT = "http://rss.nytimes.com/services/xml/rss/nyt/Technology.xml";

	private List<Article> articleList = new ArrayList<>();
	
	@Override
	public List<Article> createArticlList() {
		LOG.debug("get RSS from nytimes.");
		
		createListBySection(TODAY, ArticleSection.TODAY);
		createListBySection(POLITICS, ArticleSection.POLITICS);
		createListBySection(ECON, ArticleSection.ECON);
		createListBySection(SOCIETY, ArticleSection.SOCIETY);
		createListBySection(CULTURE, ArticleSection.CULTURE);
		createListBySection(ENT, ArticleSection.ENT);
		createListBySection(SPORT, ArticleSection.SPORT);
		createListBySection(IT, ArticleSection.IT);

		return this.articleList;
	}
	
	private void createListBySection(String rssUrl, ArticleSection section) {
		for (SyndEntry item : RSSCrawler.getArticleList(rssUrl)) {
			Article article = parseRSSItem(item, section);
			
			if (article == null) {
				continue;
			}

			if (article.getContents().length() < 150) {
				continue;
			}
			
			// create the main contents
			USContentsAnalysisService.createMainSentence(article);
			
			// add to the store
			USCollectionService.add(article);
		}
	}

	private Article parseRSSItem(SyndEntry rssItem, ArticleSection section) {
		Article article = new Article();
		article.setSection(section);
		article.setAuthor(rssItem.getAuthor());
		article.setLink(rssItem.getLink());
		article.setPubDate(new DateTime(rssItem.getPublishedDate(),	DateTimeZone.forID("Asia/Seoul")));
		
		article.setTitle(ContentsAnalysisService.clearInvalidWords(rssItem.getTitle()));
		if (Strings.isNullOrEmpty(article.getTitle()) || article.getTitle().length() < 5) {
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
