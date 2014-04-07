package com.infoc.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.infoc.crawler.kr.DaumNewsCrawler;
import com.infoc.crawler.kr.GoogleNewsCrawler;
import com.infoc.crawler.kr.NaverNewsCrawler;
import com.infoc.crawler.kr.OtherNewsCrawler;
import com.infoc.crawler.us.BostonNewsCrawler;
import com.infoc.crawler.us.ChicagoTribuneCrawler;
import com.infoc.crawler.us.LATimesCrawler;
import com.infoc.crawler.us.NYTimesCrawler;
import com.infoc.crawler.us.TimeCrawler;
import com.infoc.service.CollectionService;
import com.infoc.service.USCollectionService;
import com.infoc.util.EconInfoCrawler;

@Component
public class CrawlScheduler {
	private static final Logger LOG = LoggerFactory.getLogger(CrawlScheduler.class);
	private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
	
	@Autowired
	public DaumNewsCrawler daumNewsCrawler;
	@Autowired
	public NaverNewsCrawler naverNewsCrawler;
	@Autowired
	public GoogleNewsCrawler googleNewsCrawler;
	@Autowired
	public OtherNewsCrawler otherNewsCrawler;
	
//	@Autowired
//	public NYTimesCrawler nyTimesCrawler;
//	@Autowired
//	public ChicagoTribuneCrawler chicagoTribuneCrawler;
//	@Autowired
//	public BostonNewsCrawler bostonNewsCrawler;
//	@Autowired
//	public TimeCrawler timeCrawler;
//	@Autowired
//	public LATimesCrawler laTimesCrawler;
	
	private static List<NewsCrawler> newsCrawlerList = new ArrayList<>();
	
	private void setUpCrawlerList() {
		// kr
		newsCrawlerList.add(daumNewsCrawler);
//		newsCrawlerList.add(naverNewsCrawler);
//		newsCrawlerList.add(googleNewsCrawler);
//		newsCrawlerList.add(otherNewsCrawler);
		
		// us
//		newsCrawlerList.add(nyTimesCrawler);
//		newsCrawlerList.add(chicagoTribuneCrawler);
//		newsCrawlerList.add(bostonNewsCrawler);
//		newsCrawlerList.add(timeCrawler);
//		newsCrawlerList.add(laTimesCrawler);
	}

	private void setUpSchedules() {
		scheduledExecutorService.scheduleWithFixedDelay(new EconCrawlTask(), 0, 5, TimeUnit.MINUTES);
		scheduledExecutorService.scheduleWithFixedDelay(new CrawlClearTask(), 30, 5, TimeUnit.MINUTES);
		scheduledExecutorService.scheduleWithFixedDelay(new CrawlTask(), 0, 15, TimeUnit.MINUTES);
	}
	
	private static class CrawlTask implements Runnable {
		@Override
		public void run() {
			LOG.info("********* [START] collect the aritcles from RSS at {} ***********", 
					DateTime.now(DateTimeZone.forID("Asia/Seoul")));
			
			for (NewsCrawler crawler : newsCrawlerList) {
				try {
					crawler.createArticlList();
				} catch (Exception e) {
					LOG.error("", e);
				}
			}
			
			LOG.info("********* [END] collect the aritcles from RSS at {} ***********", 
				DateTime.now(DateTimeZone.forID("Asia/Seoul")));
		}
	}

	private static class EconCrawlTask implements Runnable {
		@Override
		public void run() {
			try {
				LOG.info("run to crawl the econ indicators.");
				
				CollectionService.ECON_INFO.putAll(EconInfoCrawler.getStock());
				CollectionService.ECON_INFO.putAll(EconInfoCrawler.getCurrency());
			} catch (Exception e) {
				LOG.error("", e);
			}
		}
	}

	private static class CrawlClearTask implements Runnable {
		@Override
		public void run() {
			LOG.info("********* [START] Clear articles one day before! at {} ***********", 
				DateTime.now(DateTimeZone.forID("Asia/Seoul")));
			
			LOG.info("Clear articles one day before!");
			CollectionService.clearYesterDay();
			USCollectionService.clearYesterDay();
			
			LOG.info("********* [END] Clear articles one day before! at {} ***********", 
				DateTime.now(DateTimeZone.forID("Asia/Seoul")));
		}
	}

	@PostConstruct
	public void runShcedule() {
		setUpCrawlerList();
		setUpSchedules();
	}
	
	@PreDestroy
	public static void cleanShcedule() {
		scheduledExecutorService.shutdownNow();
	}

}
