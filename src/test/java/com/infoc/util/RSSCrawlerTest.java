package com.infoc.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;

public class RSSCrawlerTest {
private static final Logger LOG = LoggerFactory.getLogger(RSSCrawlerTest.class);
	
	@Test
	public void test1() {
		String url = "http://media.daum.net/syndication/today_sisa.rss";
		
		for (SyndEntry item : RSSCrawler.getArticleList(url)) {
			SyndEnclosure enclosure = (SyndEnclosure)(item.getEnclosures().get(0));
			LOG.debug("{}", enclosure.getUrl());
		}
	}
}
