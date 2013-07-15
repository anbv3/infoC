/*
 * @(#)RSSReader.java $version 2013. 7. 6.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

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

/**
 * @author NBP
 */
public class RSSReader {

	public static List<SyndEntry> getArticleList(String rssUrl) {
		try {
			URL feedUrl = new URL(rssUrl);
			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(new XmlReader(feedUrl));

			List<SyndEntry> entries = (List<SyndEntry>)feed.getEntries();

			for (int i = 0; i < entries.size(); i++) {
				SyndEntry entry = entries.get(i);
			}
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
