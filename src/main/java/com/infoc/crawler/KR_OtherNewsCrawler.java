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
import com.sun.syndication.feed.synd.SyndEntry;

public class KR_OtherNewsCrawler implements NewsCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(KR_OtherNewsCrawler.class);

	private static String LIKELINK = "http://feeds.feedburner.com/likelink-recent";
	private static String NEWSTAPA = "http://www.newstapa.com/feed";
	private static String NEWSPEPPER = "http://newspeppermint.com/feed";
	private static String CLIEN_NEWS = "http://feeds.feedburner.com/Clien--news";
	private static String SLOW_NEWS = "http://feeds.feedburner.com/slownews";

	private List<Article> articleList = new ArrayList<>();

	@Override
	public List<Article> createArticlList() {
		LOG.debug("get RSS from USER.");

		createListBySection(LIKELINK, ArticleSection.OTHERS);
		createListBySection(NEWSTAPA, ArticleSection.OTHERS);
		createListBySection(NEWSPEPPER, ArticleSection.OTHERS);
		createListBySection(CLIEN_NEWS, ArticleSection.OTHERS);
		createListBySection(SLOW_NEWS, ArticleSection.OTHERS);

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
		
		Document doc;
		try {
			doc = Jsoup.connect(rssLink)
					.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
					.timeout(6000)
					.get();
		} catch (IOException e) {
			LOG.error(rssLink + "\n", e);
			return;
		}
		
		
		Elements contentsArea;
		if (rssLink.contains("newspeppermint")) {
			contentsArea = doc.select(".entry");
		} else if (rssLink.contains("clien")) {
			contentsArea = doc.select("#writeContents");
		} else if (rssLink.contains("slownews")) {
			contentsArea = doc.select("#article_content");
			
			// extract the img link //////////////////////////////////
			article.setImg(contentsArea.select("img").attr("src"));
		} else if (rssLink.contains("newstapa")) {
			contentsArea = doc.select(".entry-content");
			
			// extract the img link //////////////////////////////////
			article.setImg(contentsArea.select("img").attr("src"));
		} else {
			return;
		}
		
		article.setContents(ContentsAnalysisService.removeInvalidWordsForKR(contentsArea.text()));
	}
	
}
