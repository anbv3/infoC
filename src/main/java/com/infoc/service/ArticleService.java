package com.infoc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infoc.domain.Article;
import com.infoc.repository.ArticleRepository;

@Service
@Transactional(readOnly=true)
public class ArticleService {
	
	@Autowired
	ArticleRepository articleRepository;

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