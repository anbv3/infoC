package com.infoc.crawler.kr;

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
import com.infoc.util.RSSCrawler;
import com.sun.syndication.feed.synd.SyndEntry;

@Component
public class GoogleNewsCrawler implements NewsCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(GoogleNewsCrawler.class);

	private static String TODAY = "https://news.google.co.kr/news/feeds?hl=ko&output=rss"; // 주요기사
	private static String POLITICS = "https://news.google.co.kr/news/feeds?hl=ko&topic=p&output=rss";
	private static String ECON = "https://news.google.co.kr/news/feeds?hl=ko&topic=b&output=rss";
	private static String SOCIETY = "https://news.google.co.kr/news/feeds?hl=ko&topic=y&output=rss";
	private static String CULTURE = "https://news.google.co.kr/news/feeds?hl=ko&topic=l&output=rss";
	private static String ENT = "https://news.google.co.kr/news/feeds?hl=ko&topic=r&output=rss";
	private static String SPORT = "https://news.google.co.kr/news/feeds?hl=ko&topic=s&output=rss";
	private static String IT = "https://news.google.co.kr/news/feeds?hl=ko&topic=t&output=rss";

	private List<Article> articleList = new ArrayList<>();

	@Autowired
	public CollectionService collectionService;
	
	@Override
	public List<Article> createArticlList() {
		LOG.debug("get RSS from Google.");

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
			
			if (Strings.isNullOrEmpty(article.getContents())) {
				continue;
			}
			
			if (article.getContents().length() < 100) {
				continue;
			}
			
			DateTime currTime = new DateTime(DateTimeZone.forID("Asia/Seoul"));
			if (article.getPubDate().before(currTime.minusDays(1).toDate())) {
				return;
			}
			
			// create the main contents
			ContentsAnalysisService.createMainSentence(article);
			
			// add to the store
			collectionService.add(article);
		}
	}

	private Article parseRSSItem(SyndEntry rssItem, ArticleSection section) {
		Article article = new Article();
		article.setSection(section);
		article.setLink(rssItem.getLink());

		DateTime pubDate = new DateTime(rssItem.getPublishedDate(), DateTimeZone.forID("Asia/Seoul"));
		article.setPubDate(new Date(pubDate.getMillis()));
		article.setPubYear(pubDate.getYear());
		article.setPubMonth(pubDate.getMonthOfYear());
		article.setPubDay(pubDate.getDayOfMonth());
		article.setPubHour(pubDate.getHourOfDay());
		
		parseTitleAuthor(rssItem.getTitle(), article);
		
		// if title is empty or too short, hard to analysis. So let's skip
		if (Strings.isNullOrEmpty(article.getTitle()) || article.getTitle().length() < 5) {
			return null;
		}

		parseContentsFromLink(rssItem, article);

		return article;
	}

	private void parseTitleAuthor(String title, Article article) {
		int idx = title.lastIndexOf("-");
		article.setTitle(ContentsAnalysisService.removeInvalidWordsForKR(title.substring(0, idx).trim()));
		article.setAuthor(title.substring(idx + 1, title.length()).trim());
	}
	
	private void parseContentsFromLink(SyndEntry rssItem, Article article) {
		String rssLink = rssItem.getLink();
		
		Document doc;
		try {
			doc = Jsoup.connect(rssLink).timeout(6000).get();
		} catch (IOException e) {
			//LOG.error(rssLink + "\n", e);
			return;
		}
		
		
		Elements contentsArea;
		
		if (rssLink.contains("interview365")) {
			contentsArea = doc.select("#IDContents");
		} else if (rssLink.contains("khan")) {
			contentsArea = doc.select("#_article");
		} else if (rssLink.contains("segye")) {
			contentsArea = doc.select("#article_txt");
		} else if (rssLink.contains("asiae")) {
			contentsArea = doc.select(".article");
		} else if (rssLink.contains("chosun")) {
			contentsArea = doc.select(".par");
		} else if (rssLink.contains("dailian")) {
			contentsArea = doc.select(".par");
		} else if (rssLink.contains("newsen")) {
			contentsArea = doc.select("#CLtag");
		} else if (rssLink.contains("sportsseoul")) {
			contentsArea = doc.select("#content_area");
		} else if (rssLink.contains("fnnews")) {
			contentsArea = doc.select("#contTxt");
		} else if (rssLink.contains("kookje")) {
			contentsArea = doc.select("#news_textArea");
		} else if (rssLink.contains("hankooki")) {
			contentsArea = doc.select("#GS_Content");
		} else if (rssLink.contains("ittoday")
			|| rssLink.contains("unionpress")
			|| rssLink.contains("yonhapnews")
			|| rssLink.contains("itimes")
			|| rssLink.contains("newsis")) {

			contentsArea = doc.select("#articleBody");
		} else if (rssLink.contains("hankyung") 
			|| rssLink.contains("ahatv")) {
			
			contentsArea = doc.select("#sstvarticle");
		} else if (rssLink.contains("sportsworldi")) {
			contentsArea = doc.select("#article_content");
		} else if (rssLink.contains("tvdaily")
			|| rssLink.contains("etoday")
			|| rssLink.contains("ytn")
			|| rssLink.contains("vop")) {

			contentsArea = doc.select("#CmAdContent");
		} else {
//			LOG.error("Fail to parsing => Link:{}, ContentId:{}", rssLink, contentId);
			return;
		}
		
		article.setContents(ContentsAnalysisService.removeInvalidWordsForKR(contentsArea.text()));
		
	}
}
