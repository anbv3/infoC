package com.infoc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
@Transactional(readOnly = true)
public class ArticleService {
	private static final Logger LOG = LoggerFactory.getLogger(ArticleService.class);

	@Autowired
	ArticleRepository articleRepository;

	public List<Article> getArticles() {
		return articleRepository.findAll();
	}

	public Map<Integer, List<Article>> getArticlesByPubDateAndSection(Date date, ArticleSection section) {
		DateTime pubDate = new DateTime(date, DateTimeZone.forID("Asia/Seoul"));

		List<Article> oneDayList = articleRepository
			.findBySectionAndPubYearAndPubMonthAndPubDay(section, pubDate.getYear(), pubDate.getMonthOfYear(), pubDate.getDayOfMonth());

		Map<Integer, List<Article>> articleListMap = new LinkedHashMap<>();

		for (int i = 23; i <= 0; i--) {
			articleListMap.put(i, new ArrayList<Article>());
		}

		for (Article article : oneDayList) {
			int hour = (new DateTime(article.getPubDate(), DateTimeZone.forID("Asia/Seoul"))).getHourOfDay();
			if (articleListMap.get(hour) == null) {
				LOG.error("Why null? => hour: {}", hour);
				LOG.error("Why null? => articleListMap: {}", articleListMap);
			} else {
				articleListMap.get(hour).add(article);
			}
		}

		return articleListMap;
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