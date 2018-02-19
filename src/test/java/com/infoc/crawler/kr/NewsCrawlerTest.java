package com.infoc.crawler.kr;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;

import com.infoc.crawler.us.TimeCrawler;
import com.infoc.crawler.us.USATodayCrawler;
import com.infoc.crawler.us.WashingtonTimesCrawler;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infoc.domain.Article;
import com.infoc.service.CollectionService;
import com.infoc.service.ContentsAnalysisService;


public class NewsCrawlerTest {
	private static final Logger LOG = LoggerFactory.getLogger(DaumNewsCrawler.class);
	
	@Test
	public void test_GoogleNewsCrawler() {
		GoogleNewsCrawler d = new GoogleNewsCrawler();
		List<Article> list = d.createArticleList();

		LOG.debug("{}", list.size());
		for (Article article : list) {
			
			// get the main contents
			ContentsAnalysisService.createMainSentence(article);
			
			LOG.debug("{}", article);
			
			// add to the store
			//CollectionService.add(article);
		}
		
		for (Entry<Integer, List<Article>> entry : CollectionService.TODAY_CACHE.entrySet()) {
			LOG.debug("{}", entry.getValue().size());
			for (Article curArticle : entry.getValue()) {
				
				LOG.debug("{}", curArticle);
			}
		}
	}
	
	@Test
	public void testGetDaumContents() {
		String uuu = "http://v.media.daum.net/v/20180214140155919?d=y";

		Document doc;
		try {
			
			doc = Jsoup.connect(uuu).timeout(6000).get();
			Elements contentsArea = doc.select("#harmonyContainer");
			
			contentsArea.select(".txt_caption").remove();

			LOG.debug("{}", contentsArea.text());

		} catch (IOException e) {
			LOG.debug("", e);
		}

	}

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

    @Test
    public void test_US_NewsCrawler() {
        WashingtonTimesCrawler l = new WashingtonTimesCrawler();
        LOG.debug("{}", l.createArticleList());
    }
}
