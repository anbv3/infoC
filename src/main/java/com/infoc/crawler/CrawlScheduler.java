/*
 * @(#)CrawlScheduler.java $version 2013. 10. 31.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.crawler;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.infoc.domain.Article;
import com.infoc.service.CollectionService;
import com.infoc.service.ContentsAnalysisService;

/**
 * @author NBP
 */
public class CrawlScheduler {

	private static class CrawlTask extends TimerTask
	{
		@Override
		public void run() {
			DaumNewsCrawler d = new DaumNewsCrawler();
			List<Article> list = d.createArticlList();

			NaverNewsCrawler n = new NaverNewsCrawler();
			list.addAll(n.createArticlList());

			for (Article article : list) {
				// create the main contents
				ContentsAnalysisService.createMainSentence(article);

				// add to the store
				CollectionService.add(article);
			}
		}
	}

	public static void runShcedule() {
		Timer timer = new Timer();
		timer.schedule(new CrawlTask(), 300 * 1000);
	}

}
