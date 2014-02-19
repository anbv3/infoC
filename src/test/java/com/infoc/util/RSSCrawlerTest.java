package com.infoc.util;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RSSCrawlerTest {
private static final Logger LOG = LoggerFactory.getLogger(RSSCrawlerTest.class);

	@Test
	public void testLib() {
		String url = "http://www.nytimes.com/services/xml/rss/nyt/HomePage.xml";
		
		try {
			URL feedUrl = new URL(url);
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));
			
			List<SyndEntry> entries = (List<SyndEntry>)feed.getEntries();
			
			LOG.debug("{}", entries.size());
			
			for (SyndEntry item : entries) {
				LOG.debug("{}", item.getTitle());
				LOG.debug("{}", item.getLink());
				LOG.debug("{}", item.getDescription().getValue());
				LOG.debug("{}", item.getPublishedDate());
				LOG.debug("{}", item.getAuthor());
				LOG.debug("{}", item.getEnclosures());
			}
			
		} catch (Exception e) {
			LOG.debug("", e);
		}
		
		
	}


	@Test
	public void testToGetImgUrl() {
		String url = "http://media.daum.net/syndication/today_sisa.rss";
		
		for (SyndEntry item : RSSCrawler.getArticleList(url)) {
			SyndEnclosure enclosure = (SyndEnclosure)(item.getEnclosures().get(0));
			LOG.debug("{}", enclosure.getUrl());
		}
	}
	
	
	@Test
	public void testLATimes() {
		String url = "http://www.latimes.com/world/worldnow/la-fg-wn-lebanon-suicide-bombings-20140219,0,3839057.story#axzz2tllPQVOt";

		Document doc;
		try {
			
			doc = Jsoup.connect(url).timeout(6000).get();
			Elements contentsArea = doc.select("#story-body-text");
			LOG.debug("{}", contentsArea.text());
			
			String img = doc.select(".thumbnail").select("img").attr("src");
			LOG.debug("{}", img);

		} catch (IOException e) {
			LOG.debug("", e);
		}

	}
	
}
