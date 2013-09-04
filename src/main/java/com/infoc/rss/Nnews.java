package com.infoc.rss;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infoc.service.CollectionService;
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
public class Nnews {
	private static final Logger LOG = LoggerFactory.getLogger(Nnews.class);
	
	private static String N_NEWS = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=2";
	
	public Nnews getNews() {
		List<SyndEntry> rssList = RSSReader.getArticleList(N_NEWS);
		LOG.debug("N news size: {}", rssList.size());
		
		for(SyndEntry item : rssList) {
			Article article = parseItem(item);
			
			// Skip the news written in only English.
			if(!article.getTitle().matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
				continue;
			}
			
			CollectionService.add(article);
		}
		
		return this;
	}
	
	public Article parseItem(SyndEntry rssItem) {
		Article article = new Article();
		article.setAuthor(rssItem.getAuthor());
		article.setContents(rssItem.getDescription().getValue());
		article.setLink(rssItem.getLink());
		article.setPubDate(new DateTime(rssItem.getPublishedDate()));
		article.setTitle(rssItem.getTitle());
		
		article.setKeyWordList(Sets.newHashSet(CollectionService.SPLITTER.split(article.getTitle())));
		
		parseContents(article);
		return article;
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
