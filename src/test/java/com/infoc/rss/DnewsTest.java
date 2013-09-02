package com.infoc.rss;

import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.infoc.domain.Article;
import com.infoc.service.CollectionService;

public class DnewsTest {
	private static final Logger LOG = LoggerFactory.getLogger(DnewsTest.class);
	
	
	@Test
	public void parseTitle() {
		
		String TITLE_SPLIT_PATTERN = "\\s|\\,|\\[|\\]|\\;|\\'|\\·|\\…|\\!|\\\"|\\“|\\|”|\\.\\.";
		Splitter SPLITTER = Splitter.onPattern(TITLE_SPLIT_PATTERN).trimResults().omitEmptyStrings();
		
		String t = "[단독] 무상 학자금도 .. 모자라 저리융자 '이중지원'";
		
		LOG.debug("{}", Sets.newHashSet(SPLITTER.split(t)));
	}
	
	@Test
	public void parse() {
		Dnews dnews = new Dnews();
		dnews.getNews();

		
		for (Entry<Integer, List<Article>> entry : CollectionService.get().entrySet()) {
			for (Article curArticle : entry.getValue()) {
				
				LOG.debug("{}", curArticle.getTitle());
				LOG.debug("{}", curArticle.getKeyWordList());
				LOG.debug("{}", curArticle.getSentenceList());
				LOG.debug("-----------------------------------------");
			}
		}

	}

}
