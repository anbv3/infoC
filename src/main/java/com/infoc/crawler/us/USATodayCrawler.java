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
public class USATodayCrawler implements NewsCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(USATodayCrawler.class);
	private static String TODAY = "http://rssfeeds.usatoday.com/usatoday-newstopstories&x=1";
	private static String POLITICS = "http://rssfeeds.usatoday.com/usatodaycomwashington-topstories&x=1";
	private static String ECON = "http://rssfeeds.usatoday.com/usatodaycommoney-topstories&x=1";
	private static String ENT = "http://rssfeeds.usatoday.com/usatoday-lifetopstories&x=1";
	private static String SPORT = "http://rssfeeds.usatoday.com/usatodaycomsports-topstories&x=1";
	private static String MLB = "http://rssfeeds.usatoday.com/usatodaycommlbnl-topstories&x=1";
	private static String IT = "http://rssfeeds.usatoday.com/usatoday-techtopstories&x=1";
	
	private static String TV = "http://rssfeeds.usatoday.com/usatodaycomtelevision-topstories&x=1";
	private static String MOVIE = "http://rssfeeds.usatoday.com/usatodaycommovies-topstories&x=1";
	private static String MUSIC = "http://rssfeeds.usatoday.com/usatodaycommusic-topstories&x=1";

	private List<Article> articleList = new ArrayList<>();
	
	@Autowired
	public USCollectionService collectionService;
	
	@Override
	public List<Article> createArticleList() {
		LOG.debug("get RSS from USAToday.");
		
		createListBySection(TODAY, ArticleSection.TODAY);
		createListBySection(POLITICS, ArticleSection.POLITICS);
		createListBySection(ECON, ArticleSection.ECON);
		createListBySection(ENT, ArticleSection.ENT);
		createListBySection(SPORT, ArticleSection.SPORT);
		createListBySection(MLB, ArticleSection.SPORT);
		createListBySection(IT, ArticleSection.IT);

		createListBySection(TV, ArticleSection.CULTURE);
		createListBySection(MOVIE, ArticleSection.CULTURE);
		createListBySection(MUSIC, ArticleSection.CULTURE);

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
		
		article.setTitle(ContentsAnalysisService.removeInvalidWordsForKR(rssItem.getTitle()));
		if (Strings.isNullOrEmpty(article.getTitle()) || article.getTitle().length() < 5) {
			return null;
		}
		
		parseContentsFromLink(rssItem, article);

		return article;
	}
	
	private void parseContentsFromLink(SyndEntry rssItem, Article article) {
		String rssLink = rssItem.getLink();
		if(!rssLink.contains("usatoday")) {
			return;
		}
		
		Document doc;
		try {
			doc = Jsoup.connect(rssLink).timeout(6000).get();
		} catch (IOException e) {
			//LOG.error(rssLink + "\n", e);
			return;
		}
		
		String contentId = "div[itemprop=articleBody]";
		Elements contentsArea = doc.select(contentId);
        article.setContents(contentsArea.text());

		// extract the img link ////////////////////////////////////////////////////////
		article.setImg(contentsArea.select("img").attr("src"));
	}
}
