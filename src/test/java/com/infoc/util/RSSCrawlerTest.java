package com.infoc.util;

import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;

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
        String url = "http://www.nytimes.com/2014/06/08/us/though-not-quietly-kentucky-moves-to-cut-reliance-on-coal.html";

        Document doc;
        try {

            doc = Jsoup.connect(url)
                       .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                       .timeout(6000)
                       .get();

            LOG.debug("{}", doc.toString());
        }
        catch (IOException e) {
            LOG.debug("", e);
        }

    }

	@Test
	public void testNYTimes() {
		String url = "http://rss.nytimes.com/c/34625/f/640372/s/3b466d67/sc/7/l/0L0Snytimes0N0C20A140C0A60C0A80Cus0Cthough0Enot0Equietly0Ekentucky0Emoves0Eto0Ecut0Ereliance0Eon0Ecoal0Bhtml0Dpartner0Frss0Gemc0Frss/story01.htm";

		Document doc;
		try {

			doc = Jsoup.connect(url)
                       .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                       .timeout(6000)
                       .get();

            //LOG.debug("{}", doc.toString());

			Elements contentsArea = doc.select(".medium a");
            LOG.debug("{}", contentsArea.html());
            LOG.debug("{}", contentsArea.attr("href"));

            url = contentsArea.attr("href");
            doc = Jsoup.connect(url)
                       .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                       .timeout(6000)
                       .get();
            LOG.debug("{}", doc.toString());

            String contentId = "#story";
            contentsArea = doc.select(contentId);

            Elements bodyArea = contentsArea.select(".story-body-text");
            StringBuilder sb = new StringBuilder();
            for (Element element : bodyArea) {
                sb.append(element.ownText()).append(" ");
            }

            LOG.debug("{}", sb.toString());

//			LOG.debug("{}", contentsArea.text().replaceAll( "([\\ud800-\\udbff\\udc00-\\udfff])", ""));

			//String img = contentsArea.select("img").attr("src");
			//LOG.debug("{}", img);

		} catch (IOException e) {
			LOG.debug("", e);
		}

	}

}
