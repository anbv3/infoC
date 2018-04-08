package com.infoc.crawler;

import com.infoc.crawler.kr.DaumNewsCrawler;
import com.infoc.crawler.kr.GoogleNewsCrawler;
import com.infoc.crawler.kr.OtherNewsCrawler;
import com.infoc.crawler.us.NYTimesCrawler;
import com.infoc.crawler.us.TimeCrawler;
import com.infoc.crawler.us.USATodayCrawler;
import com.infoc.domain.Article;
import com.infoc.enumeration.ArticleSection;
import com.infoc.service.ArticleService;
import com.infoc.service.CollectionService;
import com.infoc.service.USCollectionService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class CrawlScheduler {
	private static final Logger LOG = LoggerFactory.getLogger(CrawlScheduler.class);
	private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
	
	@Autowired
	ArticleService articleService;
	
	// kr
	@Autowired
	public DaumNewsCrawler daumNewsCrawler;
	@Autowired
	public GoogleNewsCrawler googleNewsCrawler;
	@Autowired
	public OtherNewsCrawler otherNewsCrawler;
	
	// us
	@Autowired
	public USATodayCrawler usaTodayCrawler;
	@Autowired
	public NYTimesCrawler nyTimesCrawler;
	@Autowired
	public TimeCrawler timeCrawler;

	private static List<NewsCrawler> newsCrawlerList = new ArrayList<>();
	
	private void setUpCrawlerList() {
		// kr
		//newsCrawlerList.add(daumNewsCrawler);
		newsCrawlerList.add(googleNewsCrawler);
		newsCrawlerList.add(otherNewsCrawler);

		// us
        newsCrawlerList.add(timeCrawler);
        newsCrawlerList.add(usaTodayCrawler);
		newsCrawlerList.add(nyTimesCrawler);

	}

	private void setUpSchedules() {
		// After finished the task, wait for the delay and execute the task again
		scheduledExecutorService.scheduleWithFixedDelay(new CrawlTask(), 1, 10, TimeUnit.MINUTES);
		scheduledExecutorService.scheduleWithFixedDelay(new CrawlClearTask(), 10, 20, TimeUnit.MINUTES);
	}
	
	private void setUpCache() {
		DateTime today = new DateTime(DateTimeZone.forID("Asia/Seoul"));
		DateTime yesterday = today.minusDays(1);

		for (ArticleSection section : ArticleSection.values()) {
			// kr
			Map<Integer, List<Article>> krCacheList = articleService.getArticlesByPubDateAndSection(yesterday.toDate(), "KR", section);
            krCacheList.putAll(articleService.getArticlesByPubDateAndSection(today.toDate(), "KR", section));
			for (Entry<Integer, List<Article>> eachTime : krCacheList.entrySet()) {
				if (eachTime.getValue().isEmpty()) {
					continue;
				}
				
				ArticleSection.findKRCache(section.getSection())
                              .get(eachTime.getKey())
                              .addAll(eachTime.getValue());
			}
			
			// us
            Map<Integer, List<Article>> usCacheList = articleService.getArticlesByPubDateAndSection(yesterday.toDate(), "US", section);
            usCacheList.putAll(articleService.getArticlesByPubDateAndSection(today.toDate(), "US", section));
			for (Entry<Integer, List<Article>> eachTime : usCacheList.entrySet()) {
				if (eachTime.getValue().isEmpty()) {
					continue;
				}
				
				ArticleSection.findUSCache(section.getSection())
                              .get(eachTime.getKey())
                              .addAll(eachTime.getValue());
			}
    	}
		
	}
	
	private static class CrawlTask implements Runnable {
		@Override
		public void run() {
			LOG.info("********* [START] collect the articles from RSS at {} ***********",
					new DateTime(DateTimeZone.forID("Asia/Seoul")));
			
			for (NewsCrawler crawler : newsCrawlerList) {
				try {
					crawler.createArticleList();
				} catch (Exception e) {
					LOG.error("", e);
				}
			}
			
			LOG.info("********* [END] collect the articles from RSS at {} ***********",
					new DateTime(DateTimeZone.forID("Asia/Seoul")));
		}
	}

	private static class CrawlClearTask implements Runnable {
		@Override
		public void run() {
            DateTime currentTime = new DateTime(DateTimeZone.forID("Asia/Seoul"));
			LOG.info("Clear articles before! => CurrentTime: {}", currentTime);

			CollectionService.clearCache();
			USCollectionService.clearCache();
		}
	}

	@PostConstruct
	public void runSchedules() {
		setUpCache();
		setUpCrawlerList();
		setUpSchedules();
	}
	
	@PreDestroy
	public static void cleanSchedules() {
        scheduledExecutorService.shutdownNow();
    }

}
