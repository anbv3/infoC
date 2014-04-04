package com.infoc.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infoc.domain.Article;
import com.infoc.enumeration.ArticleSection;


public interface ArticleRepository extends JpaRepository<Article, Long> {
	
	// pubYear, pubMonth, pubDay
	List<Article> findBySectionAndPubYearAndPubMonthAndPubDay(ArticleSection section, int year, int month, int day);
}
