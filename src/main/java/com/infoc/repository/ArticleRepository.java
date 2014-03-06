package com.infoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infoc.domain.Article;


public interface ArticleRepository extends JpaRepository<Article, Long> {
}
