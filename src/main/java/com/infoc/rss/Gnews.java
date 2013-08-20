package com.infoc.rss;

import java.util.ArrayList;
import java.util.List;

import com.infoc.service.CollectionService;
import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infoc.domain.Article;
import com.infoc.util.RSSReader;
import com.sun.syndication.feed.synd.SyndEntry;

/**
 * @author anbv3
 */
public class Gnews {
	private static final Logger LOG = LoggerFactory.getLogger(Gnews.class);
	
	private static String G_NEWS = "https://news.google.co.kr/nwshp?hl=ko&output=rss";

	public Gnews getNews() {
		List<SyndEntry> rssList = RSSReader.getArticleList(G_NEWS);
		LOG.debug("G news size: {}", rssList.size());
		
		for (SyndEntry item : rssList) {
			Article article = parseItem(item);
			CollectionService.add(article);
		}

		return this;
	}

	public Article parseItem(SyndEntry rssItem) {
		Article article = new Article();

		article.setPubDate(new DateTime(rssItem.getPublishedDate()));
		article.setContents(rssItem.getDescription().getValue());

		parseTitleAuthor(rssItem.getTitle(), article);
		parseLink(rssItem.getLink(), article);
		parseDescrption(rssItem.getDescription().getValue(), article);

		return article;
	}

	public void parseTitleAuthor(String title, Article article) {
		int idx = title.lastIndexOf("-");
		article.setTitle(title.substring(0, idx).trim());
		article.setAuthor(title.substring(idx + 1, title.length()).trim());
	}

	public void parseLink(String link, Article article) {
		int idx = link.lastIndexOf("http");
		article.setLink(link.substring(idx));
	}

	/**
	 * extract only texts except the html tags, and pick up the longest one.
	 */
	public void parseDescrption(String desc, Article article) {
		
		StringBuffer sb = new StringBuffer();
		
		List<String> descList = new ArrayList<>();
		boolean appendFlag = false;
		for (int i = 0; i < desc.length(); i++) {
			char c = desc.charAt(i);
			if (c == '<') {
				appendFlag = false;
				descList.add(sb.toString());
				continue;
			}
			if (c == '>') {
				appendFlag = true;
				sb = new StringBuffer();
				continue;
			}

			if (appendFlag) {
				sb.append(c);
			}
		}
		descList.add(sb.toString());

		int maxLength = 0;
		int maxIdx = 0;
		for (int i = 0; i < descList.size(); i++) {
			if (maxLength < descList.get(i).length()) {
				maxLength = descList.get(i).length();
				maxIdx = i;
			}
		}

		article.setContents(StringEscapeUtils.unescapeHtml(descList.get(maxIdx)) + "...");
	}
}
