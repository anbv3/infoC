package com.infoc.crawler.kr;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infoc.domain.Article;
import com.infoc.service.CollectionService;
import com.infoc.service.ContentsAnalysisService;


/**
 * @author NBP
 */
public class DaumNewsCrawlerTest {
	private static final Logger LOG = LoggerFactory.getLogger(DaumNewsCrawler.class);
	
	@Test
	public void test1() {
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
		String uuu = "http://media.daum.net/society/affair/newsview?newsid=20140219203508919";

		Document doc;
		try {
			
			doc = Jsoup.connect(uuu).timeout(6000).get();
			Elements contentsArea = doc.select("#newsBodyShadow");
			
			contentsArea.select(".image").remove();

			LOG.debug("{}", contentsArea.text());

		} catch (IOException e) {
			LOG.debug("", e);
		}

	}
}
