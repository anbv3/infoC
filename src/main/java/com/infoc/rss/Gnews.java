package com.infoc.rss;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infoc.service.CollectionService;
import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infoc.domain.Article;
import com.infoc.domain.SentenceInfo;
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
			
			// TODO: 핵심 문장 리스트가 있는것만 추가
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

		parseKeywords(article);
		parseContents(article);
		return article;
	}

	public void parseTitleAuthor(String title, Article article) {
		int idx = title.lastIndexOf("-");
		article.setTitle(title.substring(0, idx).trim());
		article.setAuthor(title.substring(idx + 1, title.length()).trim());
	}

	public void parseLink(String link, Article article) {
		article.setLink(link);
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

	public void parseKeywords(Article article) {
		Set<String> titleList = Sets.newHashSet(CollectionService.SPLITTER.split(article.getTitle()));

		Set<String> keywordList = Sets.newHashSet();
		for (String word : titleList) {
			if (word.length() > 1) {
				keywordList.add(word);
			}
		}

		article.setKeyWordList(keywordList);
	}

	public void parseContents(Article article) {

		List<String> sList = Lists.newArrayList(
			Splitter.on(". ")
				.trimResults()
				.omitEmptyStrings()
				.split(article.getContents())
			);

		List<SentenceInfo> sentenceList = new ArrayList<>();
		for (int i = 0; i < sList.size(); i++) {
			String sentence = sList.get(i);
			SentenceInfo scInfo = new SentenceInfo();
			scInfo.setIndex(i);
			scInfo.setLength(sentence.length());
			scInfo.setSentance(sentence);
			scInfo.checkKeyword(article.getKeyWordList());

			sentenceList.add(scInfo);
		}

		article.setSentenceList(sentenceList);
	}
}
