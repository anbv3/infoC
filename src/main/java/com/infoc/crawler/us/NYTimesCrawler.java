/*
 * @(#)DaumNewsCrawler.java $version 2013. 10. 25.
 */

package com.infoc.crawler.us;

import com.google.common.base.Strings;
import com.infoc.crawler.NewsCrawler;
import com.infoc.domain.Article;
import com.infoc.enumeration.ArticleSection;
import com.infoc.service.ContentsAnalysisService;
import com.infoc.service.USCollectionService;
import com.infoc.service.USContentsAnalysisService;
import com.infoc.util.RSSCrawler;
import com.sun.syndication.feed.synd.SyndEntry;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class NYTimesCrawler implements NewsCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(NYTimesCrawler.class);
	private static String TODAY = "http://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml";
	private static String POLITICS = "http://rss.nytimes.com/services/xml/rss/nyt/Politics.xml";
	private static String ECON = "http://rss.nytimes.com/services/xml/rss/nyt/Economy.xml";
	private static String CULTURE = "http://rss.nytimes.com/services/xml/rss/nyt/FashionandStyle.xml";
	private static String ENT = "http://rss.nytimes.com/services/xml/rss/nyt/Arts.xml";
	private static String SPORT = "http://rss.nytimes.com/services/xml/rss/nyt/Sports.xml";
	private static String IT = "http://rss.nytimes.com/services/xml/rss/nyt/Technology.xml";

	private List<Article> articleList = new ArrayList<>();
	
	@Autowired
	public USCollectionService collectionService;
	
	@Override
	public List<Article> createArticleList() {
		LOG.debug("get RSS from nytimes.");
		
		createListBySection(TODAY, ArticleSection.TODAY);
//		createListBySection(POLITICS, ArticleSection.POLITICS);
//		createListBySection(ECON, ArticleSection.ECON);
//		createListBySection(CULTURE, ArticleSection.CULTURE);
//		createListBySection(ENT, ArticleSection.ENT);
//		createListBySection(SPORT, ArticleSection.SPORT);
		createListBySection(IT, ArticleSection.TODAY);

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
		article.setCountry("US");
		
		DateTime pubDate = new DateTime(rssItem.getPublishedDate(), DateTimeZone.forID("Asia/Seoul"));
		article.setPubDate(new Date(pubDate.getMillis()));
		article.setPubYear(pubDate.getYear());
		article.setPubMonth(pubDate.getMonthOfYear());
		article.setPubDay(pubDate.getDayOfMonth());
		article.setPubHour(pubDate.getHourOfDay());
		
		article.setTitle(ContentsAnalysisService.removeInvalidWordsForKR(rssItem.getTitle().trim()));
		if (Strings.isNullOrEmpty(article.getTitle()) || article.getTitle().length() < 5) {
			return null;
		}
		
		parseContentsFromLink(rssItem, article);

		return article;
	}
	
	private void parseContentsFromLink(SyndEntry rssItem, Article article) {
		String rssLink = rssItem.getLink();
		if(!rssLink.contains("nytimes")) {
			return;
		}
		
		Document doc;
		try {
			doc = Jsoup.connect(rssLink).timeout(6000).get();
		} catch (IOException e) {
			//LOG.error(rssLink + "\n", e);
			return;
		}
		
		String contentId = "#story";
		Elements contentsArea = doc.select(contentId);
		
		Elements bodyArea = contentsArea.select(".story-body-text");
		StringBuilder sb = new StringBuilder();
		for (Element element : bodyArea) {
			sb.append(element.ownText()).append(" ");
		}
		article.setContents(sb.toString());
		
		// extract the img link ////////////////////////////////////////////////////////
		article.setImg(contentsArea.select(".image > img").attr("src").trim());
	}
}
