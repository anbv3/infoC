/*
 * @(#)DaumNewsCrawler.java $version 2013. 10. 25.
 */

package com.infoc.crawler.us;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.infoc.crawler.NewsCrawler;
import com.infoc.domain.Article;
import com.infoc.enumeration.ArticleSection;
import com.infoc.service.CollectionService;
import com.infoc.service.ContentsAnalysisService;
import com.infoc.service.USCollectionService;
import com.infoc.service.USContentsAnalysisService;
import com.infoc.util.RSSCrawler;
import com.sun.syndication.feed.synd.SyndEntry;

@Component
public class BostonNewsCrawler implements NewsCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(BostonNewsCrawler.class);
	private static String TODAY = "http://feeds.boston.com/boston/topstories";
	private static String POLITICS = "http://feeds.boston.com/boston/news/politics";
	private static String ECON = "http://feeds.boston.com/boston/business";
	private static String ENT = "http://feeds.boston.com/boston/ae";
	private static String SPORT = "http://feeds.boston.com/boston/sports/news";
	private static String IT = "http://feeds.boston.com/boston/business/technology";
	
	private static String CULTURE_FOOD = "http://feeds.boston.com/boston/lifestyle/food";
	private static String CULTURE_BOOKS = "http://feeds.boston.com/boston/ae/books";

	private List<Article> articleList = new ArrayList<>();
	
	@Autowired
	public USCollectionService collectionService;
	
	@Override
	public List<Article> createArticlList() {
		LOG.debug("get RSS from Boston.");
		
		createListBySection(TODAY, ArticleSection.TODAY);
		createListBySection(POLITICS, ArticleSection.POLITICS);
		createListBySection(ECON, ArticleSection.ECON);
		createListBySection(CULTURE_FOOD, ArticleSection.CULTURE);
		createListBySection(CULTURE_BOOKS, ArticleSection.CULTURE);
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
			
			if (Strings.isNullOrEmpty(article.getContents())) {
				continue;
			}

			if (article.getContents().length() < 300) {
				continue;
			}
			
			if (article.getPubDate().before(DateTime.now(DateTimeZone.forID("Asia/Seoul")).minusDays(1).toDate())) {
				return;
			}
			
			// create the main contents
			USContentsAnalysisService.createMainSentence(article);
			
			// add to the store
			collectionService.add(article);
		}
	}

	private Article parseRSSItem(SyndEntry rssItem, ArticleSection section) {
		Article article = new Article();
		article.setSection(section);
		article.setAuthor(rssItem.getAuthor());
		article.setLink(rssItem.getLink());
		
		DateTime pubDate = new DateTime(rssItem.getPublishedDate(), DateTimeZone.forID("Asia/Seoul"));
		article.setPubDate(new Date(pubDate.getMillis()));
		article.setPubYear(pubDate.getYear());
		article.setPubMonth(pubDate.getMonthOfYear());
		article.setPubDay(pubDate.getDayOfMonth());
		article.setPubHour(pubDate.getHourOfDay());
		
		article.setTitle(ContentsAnalysisService.removeInvalidWordsForKR(rssItem.getTitle()));
		if (Strings.isNullOrEmpty(article.getTitle()) || article.getTitle().length() < 5) {
			return null;
		}
		
		parseContentsFromLink(rssItem, article);

		return article;
	}
	
	private void parseContentsFromLink(SyndEntry rssItem, Article article) {
		String rssLink = rssItem.getLink();
		if(!rssLink.contains("boston")) {
			return;
		}
		
		Document doc;
		try {
			doc = Jsoup.connect(rssLink).timeout(6000).get();
		} catch (IOException e) {
			//LOG.error(rssLink + "\n", e);
			return;
		}
		
		String contentId = ".article-text";
		Elements contentsArea = doc.select(contentId);
		String contents = contentsArea.text();
		
		if (Strings.isNullOrEmpty(contents)) {
			contentId = ".blogText";
			contentsArea = doc.select(contentId);
			contents = contentsArea.text();
		}
		
		if (Strings.isNullOrEmpty(contents)) {
			contentId = ".pic-story-content";
			contentsArea = doc.select(contentId);
			contents = contentsArea.text();
		}
		
		article.setContents(contents);
		
		
		// extract the img link ////////////////////////////////////////////////////////
		article.setImg(contentsArea.select("img").attr("src"));
	}
}
