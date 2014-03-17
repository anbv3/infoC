package com.infoc.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infoc.controller.UserController;
import com.infoc.domain.Article;
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
		LOG.debug("??: {}", article);
		Article aaa = articleRepository.save(article);
		LOG.debug("??: {}", aaa);
	}
}