/*
 * @(#)DaumNewsCrawlerTest.java $version 2013. 10. 26.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.crawler;

import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infoc.domain.Article;
import com.infoc.service.CollectionService;
import com.infoc.service.ContentsAnalysisService;


/**
 * @author NBP
 */
public class DaumNewsCrawlerTest {
	private static final Logger LOG = LoggerFactory.getLogger(DaumNewsCrawler.class);
	
	@Test
	public void test1() {
		GoogleNewsCrawler d = new GoogleNewsCrawler();
		List<Article> list = d.createArticlList();

		LOG.debug("{}", list.size());
		for (Article article : list) {
			
			// get the main contents
			ContentsAnalysisService.createMainSentence(article);
			
			LOG.debug("{}", article);
			
			// add to the store
			CollectionService.add(article);
		}
		
		for (Entry<Integer, List<Article>> entry : CollectionService.CACHE.entrySet()) {
			LOG.debug("{}", entry.getValue().size());
			for (Article curArticle : entry.getValue()) {
				
				LOG.debug("{}", curArticle);
			}
		}
	}
}
