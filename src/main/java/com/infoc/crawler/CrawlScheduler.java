package com.infoc.crawler;

import com.infoc.crawler.kr.DaumNewsCrawler;
import com.infoc.crawler.kr.GoogleNewsCrawler;
import com.infoc.crawler.kr.NaverNewsCrawler;
import com.infoc.crawler.kr.OtherNewsCrawler;
import com.infoc.crawler.us.BaseballNewsCrawler;
import com.infoc.crawler.us.LATimesCrawler;
import com.infoc.crawler.us.TimeCrawler;
import com.infoc.crawler.us.USATodayCrawler;
import com.infoc.crawler.us.WashingtonTimesCrawler;
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
	private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
	
	@Autowired
	ArticleService articleService;
	
	// kr
	@Autowired
	public DaumNewsCrawler daumNewsCrawler;
	@Autowired
	public NaverNewsCrawler naverNewsCrawler;
	@Autowired
	public GoogleNewsCrawler googleNewsCrawler;
	@Autowired
	public OtherNewsCrawler otherNewsCrawler;
	
	// us
	@Autowired
	public WashingtonTimesCrawler washingtonTimesCrawler;
	@Autowired
	public USATodayCrawler usaTodayCrawler;
	@Autowired
	public TimeCrawler timeCrawler;
	@Autowired
	public LATimesCrawler laTimesCrawler;
    @Autowired
	public BaseballNewsCrawler baseballNewsCrawler;
	
	private static List<NewsCrawler> newsCrawlerList = new ArrayList<>();
	
	private void setUpCrawlerList() {
		// kr
		newsCrawlerList.add(daumNewsCrawler);
		newsCrawlerList.add(naverNewsCrawler);
		newsCrawlerList.add(googleNewsCrawler);
		newsCrawlerList.add(otherNewsCrawler);

		// us
		newsCrawlerList.add(timeCrawler);
        newsCrawlerList.add(usaTodayCrawler);
        newsCrawlerList.add(washingtonTimesCrawler);
		newsCrawlerList.add(laTimesCrawler);
		newsCrawlerList.add(baseballNewsCrawler);

	}

	private void setUpSchedules() {
		// After finished the task, wait for the delay and execute the task again
		scheduledExecutorService.scheduleWithFixedDelay(new CrawlTask(), 0, 5, TimeUnit.MINUTES);
		scheduledExecutorService.scheduleWithFixedDelay(new CrawlClearTask(), 10, 1, TimeUnit.MINUTES);
	}
	
	private void setUpCache() {
		DateTime today = new DateTime(DateTimeZone.forID("Asia/Seoul"));
		
		for (ArticleSection section : ArticleSection.values()) {
			// kr
			Map<Integer, List<Article>> cacheList = articleService.getArticlesByPubDateAndSection(today.toDate(), "KR", section);
			for (Entry<Integer, List<Article>> eachTime : cacheList.entrySet()) {
				if (eachTime.getValue().isEmpty()) {
					continue;
				}
				
				ArticleSection.findKRCache(section.getSection()).get(eachTime.getKey()).addAll(eachTime.getValue());
			}
			
			// us
			cacheList = articleService.getArticlesByPubDateAndSection(today.toDate(), "US", section);
			for (Entry<Integer, List<Article>> eachTime : cacheList.entrySet()) {
				if (eachTime.getValue().isEmpty()) {
					continue;
				}
				
				ArticleSection.findUSCache(section.getSection()).get(eachTime.getKey()).addAll(eachTime.getValue());
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
			LOG.info("Clear articles one day before! => CurrentTime: {}", currentTime);

            // actually remove the articles published 25 hours ago
			CollectionService.clearYesterday();
			USCollectionService.clearYesterday();
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
