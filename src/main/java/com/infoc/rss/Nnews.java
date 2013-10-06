package com.infoc.rss;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infoc.domain.Article;
import com.infoc.service.CollectionService;
import com.infoc.util.RSSCrawler;
import com.sun.syndication.feed.synd.SyndEntry;

/**
 * @author anbv3
 */
public class Nnews {
	private static final Logger LOG = LoggerFactory.getLogger(Nnews.class);

	private static String N_NEWS = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=2";
	private static String N_NEWS_POPULAR = "http://feeds.feedburner.com/naver_news_popular";

	public Nnews getNews() {
		List<SyndEntry> rssList = RSSCrawler.getArticleList(N_NEWS);
		LOG.debug("N news size: {}", rssList.size());

		for (SyndEntry item : rssList) {
			Article article = parseItem(item);

			if (article == null) {
				continue;
			}

			CollectionService.add(article);
		}

		return this;
	}

	public Article parseItem(SyndEntry rssItem) {

		// Skip the news written in only English.
		if (!rssItem.getTitle().matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
			return null;
		}

		Article article = new Article();
		article.setAuthor(rssItem.getAuthor());
		article.setLink(rssItem.getLink());
		article.setPubDate(new DateTime(rssItem.getPublishedDate()));
		article.setTitle(rssItem.getTitle());
		article.createKeyWorkList();
		article.setContents(rssItem.getDescription().getValue());
		
		parseDescrption(article);
		
		article.extractMainContents();

		return article;
	}
	

	private String getOriginalContents(Article article) {
		try {
			String contentId = "";
			if(article.getLink().contains("news.naver.com")) {
				contentId = "#articleBody"; 
			} else if (article.getLink().contains("interview365.com")) {
				contentId = "#IDContents"; 
			} else if (article.getLink().contains("www.ittoday.co.kr")) {
				contentId = "#articleBody"; 
			} else if (article.getLink().contains("www.sportsworldi.com")) {
				contentId = "#article_content"; 
			} else if (article.getLink().contains("tvdaily.mk.co.kr") || 
				article.getLink().contains("etoday.co.kr") ||
				article.getLink().contains("vop.co.kr") ) {
				
				contentId = "#CmAdContent"; 
			} else {
				return article.getContents();
			}
			
			Document doc = Jsoup.connect(article.getLink()).get();
			Elements newsHeadlines = doc.select(contentId);
			
			return newsHeadlines.text();
		} catch (IOException e) {
			LOG.error("", e);
			return "";
		}
	}

	public void parseDescrption(Article article) {
		String desc = getOriginalContents(article).replaceAll("&nbsp;", "");
		
		
		StringBuffer sb = new StringBuffer();
		boolean appendFlag = true;
		for (int i = 0; i < desc.length(); i++) {
			char c = desc.charAt(i);
			if (c == '【') {
				appendFlag = false;
				continue;
			}
			if (c == '】') {
				appendFlag = true;
				continue;
			}

			if (appendFlag) {
				sb.append(c);
			}
		}

		article.setContents(sb.toString());
	}
}
