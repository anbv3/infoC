package com.infoc.service;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infoc.domain.Article;
import com.infoc.enumeration.ArticleSection;
import com.infoc.repository.ArticleRepository;

@Service
@Transactional(readOnly=true)
public class ArticleService {
	private static final Logger LOG = LoggerFactory.getLogger(ArticleService.class);
	
	@Autowired
	ArticleRepository articleRepository;

	public List<Article> getArticles() {
		return articleRepository.findAll();
	}
	
	public List<Article> getArticlesByPubDateAndSection(Date date, ArticleSection section) {
		DateTime pubDate = new DateTime(date, DateTimeZone.forID("Asia/Seoul"));
		
		return articleRepository.findBySectionAndPubYearAndPubMonthAndPubDay(section, pubDate.getYear(), pubDate.getMonthOfYear(), pubDate.getDayOfMonth());
	}
	
	public Page<Article> getArticles(Pageable pageable) {
		return articleRepository.findAll(pageable);
	}

	public Article getArticle(Long id) {
		return articleRepository.findOne(id);
	}

	@Transactional
	public void delete(Long id) {
		articleRepository.delete(id);		
	}

	@Transactional
	public void add(Article article) {
		articleRepository.save(article);
	}
}