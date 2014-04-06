/*
 * @(#)DaumNewsCrawler.java $version 2013. 10. 25.
 */

package com.infoc.crawler.kr;

import java.io.IOException;
import java.util.ArrayList;
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
public class NaverNewsCrawler implements NewsCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(NaverNewsCrawler.class);
	private static String TODAY = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=3";
	private static String POLITICS = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=4";
	private static String ECON = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=5";
	private static String SOCIETY = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=6";
	private static String CULTURE = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=7";
	private static String ENT = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=10";
	private static String SPORT = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=11";
	private static String IT = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=9";

	private List<Article> articleList = new ArrayList<>();

	@Autowired
	public CollectionService collectionService;
	
	@Override
	public List<Article> createArticlList() {
		LOG.debug("get RSS from Naver.");
		
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
			
			if (article.getPubDate().before(DateTime.now(DateTimeZone.forID("Asia/Seoul")).minusDays(1).toDate())) {
				return;
			}
			
			// create the main contents
			ContentsAnalysisService.createMainSentence(article);
			
			// add to the store
			collectionService.add(article);
		}
	}

	private Article parseRSSItem(SyndEntry rssItem, ArticleSection section) {
		// Skip the news written in only English.
		if (!rssItem.getTitle().matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
			return null;
		}

		Article article = new Article();
		article.setSection(section);
		article.setAuthor(rssItem.getAuthor());
		article.setLink(rssItem.getLink());
		
		DateTime pubDate = new DateTime(rssItem.getPublishedDate(), DateTimeZone.forID("Asia/Seoul"));
		article.setPubDate(pubDate.toDate());
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
		if(!rssLink.contains("naver")) {
			return;
		}
		
		String contentId = "#articleBody";
		
		Document doc;
		try {
			doc = Jsoup.connect(rssLink).timeout(6000).get();
		} catch (IOException e) {
			//LOG.error(rssLink + "\n", e);
			return;
		}
		
		Elements contentsArea = doc.select(contentId);
		
		// 본문영역 뒤에 신문사 기사링크 영역 제거
		Elements linkArea = doc.select(".link_news");
		int linkIdx = contentsArea.text().indexOf(linkArea.text());
		if (linkIdx != -1) {
			article.setContents(contentsArea.text().substring(0, linkIdx));
		} else {
			article.setContents(contentsArea.text());
		}
		
		article.setContents(ContentsAnalysisService.removeInvalidWordsForKR(article.getContents()));
		
		// extract the img link ///////////////////////////////////////////////////
		article.setImg(contentsArea.select("img").attr("src"));
	}
	
}
