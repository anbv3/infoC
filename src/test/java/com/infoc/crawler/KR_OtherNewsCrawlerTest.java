package com.infoc.crawler;

import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class KR_OtherNewsCrawlerTest {
	private static final Logger LOG = LoggerFactory.getLogger(KR_OtherNewsCrawlerTest.class);
	
	@Test
	public void testLib() {
		String url = "http://feeds.feedburner.com/likelink-recent";
		
		try {
			URL feedUrl = new URL(url);
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));
			
			List<SyndEntry> entries = (List<SyndEntry>)feed.getEntries();
			
			LOG.debug("{}", entries.size());
			
			for (SyndEntry item : entries) {
				LOG.debug("getTitle: {}", item.getTitle());
				LOG.debug("getLink: {}", item.getLink());
				LOG.debug("getDescription: {}", item.getDescription().getValue());
				LOG.debug("getPublishedDate: {}", item.getPublishedDate());
				LOG.debug("{}", item.getAuthor());
				LOG.debug("{}", item.getEnclosures());
			}
			
		} catch (Exception e) {
			LOG.debug("", e);
		}
		
		
	}
}
