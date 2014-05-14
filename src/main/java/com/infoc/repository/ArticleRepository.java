package com.infoc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.infoc.domain.Article;
import com.infoc.domain.User;
import com.infoc.enumeration.ArticleSection;


public interface ArticleRepository extends JpaRepository<Article, Long> {
	
	// pubYear, pubMonth, pubDay
	List<Article> findByCountryAndSectionAndPubYearAndPubMonthAndPubDay(String country, ArticleSection section, int year, int month, int day, Sort sort);
	
	Page<Article> findByCountryAndSectionAndMainContentsLike(String country, ArticleSection section, String query, Pageable pageable);
}
