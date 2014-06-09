package com.infoc.util;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RSSCrawler {
    private static final Logger LOG = LoggerFactory.getLogger(RSSCrawler.class);

	public static List<SyndEntry> getArticleList(String rssUrl) {
		try {
			URL feedUrl = new URL(rssUrl);
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));

			List<SyndEntry> entries = (List<SyndEntry>)feed.getEntries();
            LOG.debug("RSS size: {}", entries.size());

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
