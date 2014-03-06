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
		String url = "http://feeds.feedburner.com/likelink-recent";

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

				LOG.debug("{}", Jsoup.parse(item.getDescription().getValue()).select("ul").text());
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
		String url = "http://slownews.kr/20194?utm_source=feedburner&utm_medium=feed&utm_campaign=Feed%3A+slownews+%28%EC%8A%AC%EB%A1%9C%EC%9A%B0%EB%89%B4%EC%8A%A4%29";

		Document doc;
		try {

			doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0").timeout(6000).get();
			Elements contentsArea = doc.select("#article_content");
			LOG.debug("{}", contentsArea.text());

			String img = doc.select(".thumbnail").select("img").attr("src");
			LOG.debug("{}", img);

		} catch (IOException e) {
			LOG.debug("", e);
		}

	}

}
