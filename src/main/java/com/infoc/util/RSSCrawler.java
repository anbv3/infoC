package com.infoc.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class RSSCrawler {

	public static List<SyndEntry> getArticleList(String rssUrl) {
		try {
			URL feedUrl = new URL(rssUrl);
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));

			List<SyndEntry> entries = (List<SyndEntry>)feed.getEntries();

			return entries;
		} catch (IllegalArgumentException e) {
			// ...
		} catch (FeedException e) {
			// ...
		} catch (IOException e) {
			// ...
		}

		return new ArrayList<SyndEntry>();
	}
}
