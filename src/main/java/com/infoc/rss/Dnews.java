package com.infoc.rss;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infoc.domain.Article;
import com.infoc.domain.SentenceInfo;
import com.infoc.service.CollectionService;
import com.infoc.util.RSSReader;
import com.sun.syndication.feed.synd.SyndEntry;

/**
 * @author anbv3
 */
public class Dnews {
	private static final Logger LOG = LoggerFactory.getLogger(Dnews.class);

	private static String RSS_URL = "http://media.daum.net/syndication/today_sisa.rss";

	public Dnews getNews() {
		List<SyndEntry> rssList = RSSReader.getArticleList(RSS_URL);
		LOG.debug("D News size: {}", rssList.size());

		for (SyndEntry item : rssList) {
			Article article = parseItem(item);
			
			CollectionService.add(article);
		}

		return this;
	}

	public Article parseItem(SyndEntry rssItem) {
		Article article = new Article();
		article.setAuthor(rssItem.getAuthor());
		article.setLink(rssItem.getLink());
		article.setPubDate(new DateTime(rssItem.getPublishedDate()));
		article.setTitle(rssItem.getTitle());
		
		article.setKeyWordList(Sets.newHashSet(CollectionService.SPLITTER.split(article.getTitle())));
		
		parseDescrption(rssItem.getDescription().getValue(), article);
		parseContents(article);
		
		return article;
	}

	/**
	 * daum news includes some special characters, so skip them.
	 */
	public void parseDescrption(String desc, Article article) {
		StringBuffer sb = new StringBuffer();

		boolean appendFlag = true;
		for (int i = 0; i < desc.length(); i++) {
			char c = desc.charAt(i);
			if (c == '[' || c == '【') {
				appendFlag = false;
				continue;
			}
			if (c == ']' || c == '】') {
				appendFlag = true;
				continue;
			}

			if (appendFlag) {
				sb.append(c);
			}
		}

		article.setContents(sb.append("...").toString());
	}

	public void parseContents(Article article) {

		List<String> sList = Lists.newArrayList(
			Splitter.on(".")
				.trimResults()
				.omitEmptyStrings()
				.split(article.getContents())
			);

		List<SentenceInfo> sentenceList = new ArrayList<>();
		for (int i=0 ; i<sList.size() ; i++) {
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
