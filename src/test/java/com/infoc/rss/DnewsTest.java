package com.infoc.rss;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infoc.domain.Article;
import com.infoc.domain.SentenceInfo;
import com.infoc.service.CollectionService;

public class DnewsTest {
	private static final Logger LOG = LoggerFactory.getLogger(DnewsTest.class);

	@Test
	public void parseTitle() {

		String TITLE_SPLIT_PATTERN = "\\s|\\,|\\[|\\]|\\;|\\'|\\·|\\…|\\!|\\\"|\\“|\\|”|\\.\\.";
		Splitter SPLITTER = Splitter.onPattern(TITLE_SPLIT_PATTERN).trimResults().omitEmptyStrings();

		String t = "[단독] 무상 학자금도 .. 모자라 저리융자 '이중지원'";

		LOG.debug("{}", Sets.newHashSet(SPLITTER.split(t)));
	}

	@Test
	public void parseSentenceList() {
		Dnews dnews = new Dnews();
		dnews.getNews();

		for (Entry<Integer, List<Article>> entry : CollectionService.get().entrySet()) {
			for (Article curArticle : entry.getValue()) {

				LOG.debug("{}", curArticle.getTitle());
				LOG.debug("{}", curArticle.getKeyWordList());

				for (SentenceInfo sc : curArticle.getSentenceList()) {
					LOG.debug("{}", sc.toString());
				}
				LOG.debug("-----------------------------------------");
			}
		}
	}
	
	@Test
	public void testGetOriginalContents() {
		String uuu = "http://media.daum.net/economic/others/newsview?newsid=20131003175005632";
		
		Document doc;
		try {
			doc = Jsoup.connect(uuu).get();
			Elements newsHeadlines = doc.select("#newsBodyShadow");
			LOG.debug("{}", newsHeadlines.toString());
			LOG.debug("{}", newsHeadlines.text());
			LOG.debug("{}", newsHeadlines.html());
			
			
			List<String> sList = Lists.newArrayList(Splitter.on(". ").trimResults()
				.omitEmptyStrings().split(newsHeadlines.text()));
			for(String s : sList) {
				LOG.debug("{}", s);
			}
			
		} catch (IOException e) {
			LOG.debug("", e);
		}
	
	}

}
