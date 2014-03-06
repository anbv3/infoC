/*
 * @(#)DaumNewsCrawler.java $version 2013. 10. 25.
 */

package com.infoc.crawler;

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

import com.google.common.base.Strings;
import com.infoc.domain.Article;
import com.infoc.enumeration.ArticleSection;
import com.infoc.service.CollectionService;
import com.infoc.service.ContentsAnalysisService;
import com.infoc.util.RSSCrawler;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;

public class KR_DaumNewsCrawler implements NewsCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(KR_DaumNewsCrawler.class);

	private static String TODAY = "http://media.daum.net/syndication/today_sisa.rss";
	private static String POLITICS = "http://media.daum.net/syndication/politics.rss";
	private static String ECON = "http://media.daum.net/syndication/economic.rss";
	private static String SOCIETY = "http://media.daum.net/syndication/society.rss";
	private static String CULTURE = "http://media.daum.net/syndication/culture.rss";
	private static String ENT = "http://media.daum.net/syndication/today_entertain.rss";
	private static String SPORT = "http://media.daum.net/syndication/today_sports.rss";
	private static String IT = "http://media.daum.net/syndication/digital.rss";

	private List<Article> articleList = new ArrayList<>();

	@Override
	public List<Article> createArticlList() {
		LOG.debug("get RSS from Daum.");

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
			
			if (article.getPubDate().isBefore(DateTime.now(DateTimeZone.forID("Asia/Seoul")).minusDays(1))) {
				return;
			}
			
			// create the main contents
			ContentsAnalysisService.createMainSentence(article);
			
			// add to the store
			CollectionService.add(article);
		}
	}

	private Article parseRSSItem(SyndEntry rssItem, ArticleSection section) {
		Article article = new Article();
		article.setSection(section);
		article.setAuthor(rssItem.getAuthor());
		article.setLink(rssItem.getLink());
		article.setPubDate(new DateTime(rssItem.getPublishedDate(), DateTimeZone.forID("Asia/Seoul")));
		article.setTitle(ContentsAnalysisService.removeInvalidWordsForKR(rssItem.getTitle()));
		if (Strings.isNullOrEmpty(article.getTitle()) || article.getTitle().length() < 5) {
			return null;
		}

		parseContentsFromLink(rssItem, article);

		return article;
	}
	
	private void parseContentsFromLink(SyndEntry rssItem, Article article) {
		String rssLink = rssItem.getLink();
		if(!rssLink.contains("daum")) {
			return;
		}
		
		Document doc;
		try {
			doc = Jsoup.connect(rssLink).timeout(6000).get();
		} catch (IOException e) {
			//LOG.error(rssLink + "\n", e);
			return;
		}
		
		String contentId = "#newsBodyShadow";
		Elements contentsArea = doc.select(contentId);
		
		// remove the .image tag because the title is existed again as the caption of the img.
		contentsArea.select(".image").remove();
		
		article.setContents(ContentsAnalysisService.removeInvalidWordsForKR(contentsArea.text()));
		
		
		// extract the img link ////////////////////////////////////////////////////////
		if (!rssItem.getEnclosures().isEmpty()) {
			SyndEnclosure enclosure = (SyndEnclosure)(rssItem.getEnclosures().get(0));
			article.setImg(enclosure.getUrl());
		}
	}
	
}
