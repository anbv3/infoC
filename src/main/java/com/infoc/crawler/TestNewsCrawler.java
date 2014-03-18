/*
 * @(#)DaumNewsCrawler.java $version 2013. 10. 25.
 */

package com.infoc.crawler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.infoc.domain.Article;
import com.infoc.repository.ArticleRepository;
import com.infoc.service.CollectionService2;

@Service
public class TestNewsCrawler implements NewsCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(TestNewsCrawler.class);

	@Autowired
	private CollectionService2 collectionService2;

	@Autowired
	private ArticleRepository articleRepository;
	
	/**
	 * @return
	 * @see com.infoc.crawler.NewsCrawler#createArticlList()
	 */
	@Override
	public List<Article> createArticlList() {
		articleRepository.save(new Article());
		collectionService2.add(new Article());
		return null;
	}
	
}
