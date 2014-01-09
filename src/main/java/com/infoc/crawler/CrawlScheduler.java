package com.infoc.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.infoc.service.CollectionService;
import com.infoc.util.EconInfoCrawler;

@Component
public class CrawlScheduler {
	private static final Logger LOG = LoggerFactory.getLogger(CrawlScheduler.class);

	private static List<NewsCrawler> newsCrawlerList = new ArrayList<>();
	static {
		newsCrawlerList.add(new DaumNewsCrawler());
		newsCrawlerList.add(new NaverNewsCrawler());
		newsCrawlerList.add(new GoogleNewsCrawler());
	}

	private static class CrawlTask extends TimerTask {
		@Override
		public void run() {
			LOG.info("collect the aritcles from RSS.");
			for (NewsCrawler crawler : newsCrawlerList) {
				crawler.createArticlList();
			}
		}
	}

	private static class EconCrawlTask extends TimerTask {
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

	private static class CrawlClearTask extends TimerTask {
		@Override
		public void run() {
			LOG.info("Clear articles one day before!");
			CollectionService.clearYesterDay();
		}
	}

	@PostConstruct
	public static void runShcedule() {
		new Timer().scheduleAtFixedRate(new EconCrawlTask(), 1000, 5 * 60 * 1000);
		new Timer().scheduleAtFixedRate(new CrawlTask(), 2000, 10 * 60 * 1000);
		new Timer().scheduleAtFixedRate(new CrawlClearTask(), 10000, 60 * 60 * 1000);
	}

}
