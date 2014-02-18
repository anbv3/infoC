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
import org.springframework.stereotype.Component;

import com.infoc.service.CollectionService;
import com.infoc.service.USCollectionService;
import com.infoc.util.EconInfoCrawler;

@Component
public class CrawlScheduler {
	private static final Logger LOG = LoggerFactory.getLogger(CrawlScheduler.class);
	private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
	
	private static List<NewsCrawler> newsCrawlerList = new ArrayList<>();
	static {
		newsCrawlerList.add(new DaumNewsCrawler());
		newsCrawlerList.add(new NaverNewsCrawler());
		newsCrawlerList.add(new GoogleNewsCrawler());
		newsCrawlerList.add(new US_NYTimesCrawler());
		newsCrawlerList.add(new US_ChicagoTribuneCrawler());
		newsCrawlerList.add(new US_BostonNewsCrawler());
	}

	private static class CrawlTask implements Runnable {
		@Override
		public void run() {
			LOG.info("collect the aritcles from RSS at {} O'clock", 
					DateTime.now(DateTimeZone.forID("Asia/Seoul")).getHourOfDay());
			
			for (NewsCrawler crawler : newsCrawlerList) {
				try {
					crawler.createArticlList();
				} catch (Exception e) {
					LOG.error("", e);
				}
			}
		}
	}

	private static class EconCrawlTask implements Runnable {
		@Override
		public void run() {
			try {
				LOG.info("collect the info for econ indicator.");
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
			LOG.info("Clear articles one day before!");
			CollectionService.clearYesterDay();
			USCollectionService.clearYesterDay();
		}
	}

	@PostConstruct
	public static void runShcedule() {
		scheduledExecutorService.scheduleAtFixedRate(new EconCrawlTask(), 0, 5, TimeUnit.MINUTES);
		scheduledExecutorService.scheduleAtFixedRate(new CrawlTask(), 0, 15, TimeUnit.MINUTES);
		scheduledExecutorService.scheduleAtFixedRate(new CrawlClearTask(), 30, 30, TimeUnit.MINUTES);
	}
	
	@PreDestroy
	public static void cleanShcedule() {
		scheduledExecutorService.shutdownNow();
	}

}
