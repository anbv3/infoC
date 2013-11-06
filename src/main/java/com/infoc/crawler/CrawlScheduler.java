/*
 * @(#)CrawlScheduler.java $version 2013. 10. 31.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infoc.domain.Article;
import com.infoc.service.CollectionService;
import com.infoc.service.ContentsAnalysisService;

/**
 * @author NBP
 */
public class CrawlScheduler {
	private static final Logger LOG = LoggerFactory.getLogger(CrawlScheduler.class);

	private static List<NewsCrawler> newsCrawlerList = new ArrayList<>();
	static {
		newsCrawlerList.add(new DaumNewsCrawler());
		newsCrawlerList.add(new NaverNewsCrawler());
		newsCrawlerList.add(new GoogleNewsCrawler());
	}
	
	private static class CrawlTask extends TimerTask
	{
		@Override
		public void run() {
			 
			List<Article> articleList = new ArrayList<>();
			for (NewsCrawler crawler : newsCrawlerList) {
				articleList.addAll(crawler.createArticlList());
			}

			for (Article article : articleList) {
				// create the main contents
				ContentsAnalysisService.createMainSentence(article);

				// add to the store
				CollectionService.add(article);
			}
			
			CollectionService.clearYesterDay();
		}
	}
	
	private static class CrawlClearTask extends TimerTask
	{
		@Override
		public void run() {
			LOG.info("Clear articles one day before!");
			CollectionService.clearYesterDay();
		}
	}

	public static void runShcedule() {
		Timer timer = new Timer();
		timer.schedule(new CrawlTask(), 1000, 10*60*1000);
		timer.schedule(new CrawlClearTask(), 1000, 60*60*1000);
	}

}
