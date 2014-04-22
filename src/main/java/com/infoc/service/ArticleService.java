package com.infoc.service;

import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.data.domain.Sort;
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

	private Sort sortByHour() {
        return new Sort(Sort.Direction.DESC, "pubHour");
    }
	
	/**
	 * DB에서 section과 날자로 기사를 조회
	 */
	public Map<Integer, List<Article>> getArticlesByPubDateAndSection(Date date, ArticleSection section) {
		DateTime pubDate = new DateTime(date, DateTimeZone.forID("Asia/Seoul"));

		// 각 section과 날자별 기사를 조회하고 시간 순으로 정렬
		List<Article> oneDayList = articleRepository.findBySectionAndPubYearAndPubMonthAndPubDay(
			section, pubDate.getYear(), pubDate.getMonthOfYear(), pubDate.getDayOfMonth(), sortByHour());
		
		// Map<시간, 기사>의 형태로 변경하여 리턴
		Map<Integer, List<Article>> articleListMap = new LinkedHashMap<>();
		for (Article article : oneDayList) {
			int hour = (new DateTime(article.getPubDate(), DateTimeZone.forID("Asia/Seoul"))).getHourOfDay();
			if (articleListMap.get(hour) == null) {
				List<Article> tmpList = new ArrayList<Article>();
				tmpList.add(article);
				articleListMap.put(hour, tmpList);
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