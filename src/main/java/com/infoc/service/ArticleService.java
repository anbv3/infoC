package com.infoc.service;

import com.infoc.domain.Article;
import com.infoc.enumeration.ArticleSection;
import com.infoc.repository.ArticleRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    static String convert(String str, String encoding) throws IOException {
        ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
        requestOutputStream.write(str.getBytes(encoding));
        return requestOutputStream.toString(encoding);
    }

	/**
	 * DB에서 section과 날자로 기사를 조회
	 */
	public Map<Integer, List<Article>> getArticlesByPubDateAndSection(Date date, String country, ArticleSection section) {
		DateTime pubDate = new DateTime(date, DateTimeZone.forID("Asia/Seoul"));

		// 각 section과 날자별 기사를 조회하고 시간 순으로 정렬
        List<Article> oneDayList = articleRepository.findByCountryAndSectionAndPubYearAndPubMonthAndPubDay(
				country, section, pubDate.getYear(), pubDate.getMonthOfYear(), pubDate.getDayOfMonth(), sortByHour());

		// Map<시간, 기사>의 형태로 변경하여 리턴
		Map<Integer, List<Article>> articleListMap = new LinkedHashMap<>();
		if (oneDayList == null) {
			return articleListMap;
		}

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

	public Page<Article> getArticleList(Pageable pageable) {
		return articleRepository.findAll(pageable);
	}

	public Page<Article> getArticlesByMainContents(String country, ArticleSection section, String query, Pageable pageable) {
		String likeQuery = '%' + query.trim() + '%';
		Pageable page = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "pubDate");

		Page<Article> list = articleRepository.findByCountryAndSectionAndMainContentsLike(country, section, likeQuery, page);
		return list;
	}

	public Map<String, String> extractStartAndEndDate(Page<Article> articlePage) {
		Map<String, String> pubDateMap = new HashMap<String, String>();
		if(articlePage.getNumberOfElements() == 0) {
			pubDateMap.put("start", "");
			pubDateMap.put("end", "");
			return pubDateMap;
		}

		List<Article> articleList = articlePage.getContent();

		DateTime startTime = new DateTime(articleList.get(0).getPubDate(), DateTimeZone.forID("Asia/Seoul"));
		pubDateMap.put("start", startTime.toString(DateTimeFormat.forPattern("yyyy/MM/dd HH:mm")));

		DateTime endTime = new DateTime(articleList.get(articleList.size() -1).getPubDate(), DateTimeZone.forID("Asia/Seoul"));
		pubDateMap.put("end", endTime.toString(DateTimeFormat.forPattern("yyyy/MM/dd HH:mm")));

		return pubDateMap;
	}

	public Article getArticle(Long id) {
		return articleRepository.findOne(id);
	}

    @Transactional(readOnly = true)
    public List<Article> getArticleByTitleAndLink(String title, String link) {
        return articleRepository.findByTitleAndLink(title, link);
    }


	@Transactional
	public void delete(Long id) {
		articleRepository.delete(id);
	}

	@Transactional
	public Article add(Article article) {
		try {

            List<Article> articleList = articleRepository.findByTitleAndLink(article.getTitle(), article.getLink());
            if (articleList != null || !articleList.isEmpty()) {
                return null;
            }

			return articleRepository.save(article);
		} catch(Exception e) {
			LOG.error("ERROR: {} ==> {}", e.getCause(), article);
			LOG.error("ERROR: {}", new String(article.getMainContents().getBytes()));
		}
		return null;
	}

    @Transactional
    public void update(Article article) {
        if (article.getId() == null || article.getId() == 0L) {
            return;
        }

        try {
            articleRepository.save(article);
        } catch(Exception e) {
            LOG.error("", e);
        }

    }
}