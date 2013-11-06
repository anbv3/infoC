package com.infoc.util;

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
import com.infoc.crawler.DaumNewsCrawler;
import com.infoc.domain.Article;
import com.infoc.domain.SentenceInfo;
import com.infoc.service.CollectionService;
import com.infoc.service.ContentsAnalysisService;

public class ParseTest {
	private static final Logger LOG = LoggerFactory.getLogger(ParseTest.class);
	
	@Test
	public void parseTitleSP() {
		String t = "[단독] 무상 학자금도 .. 모자라 저리융자 '이중지원'";
		LOG.debug("-{}", t.replaceAll("\\[.*\\]", ""));
	}
	
	@Test
	public void parseTitle() {

		String TITLE_SPLIT_PATTERN = "\\s|\\,|\\[|\\]|\\;|\\'|\\·|\\…|\\!|\\\"|\\“|\\|”|\\.\\.";
		Splitter SPLITTER = Splitter.onPattern(TITLE_SPLIT_PATTERN).trimResults().omitEmptyStrings();

		String t = "[단독] 무상 학자금도 .. 모자라 저리융자 '이중지원'";

		LOG.debug("{}", Sets.newHashSet(SPLITTER.split(t)));
	}
	
	@Test
	public void parseSentence() {
		String contents = " 시구를 위해 마운드로 이동하고 있다.hany@media.sportsseoul.com";
		
		List<String> sList = Lists.newArrayList(
			Splitter.onPattern("\\.\\s|다\\.")
				.trimResults()
				.omitEmptyStrings()
				.split(contents)
			);
		

		LOG.debug("{}", sList);
		
		
		String a = "알려졌다.2013.11.1/뉴스1";
		
		
		LOG.debug("{}", a.replaceAll("다\\.","다\\. "));
	}

	@Test
	public void parseSentenceList() {
		DaumNewsCrawler d = new DaumNewsCrawler();
		List<Article> list = d.createArticlList();
		for (Article article : list) {
			// create the main contents
			ContentsAnalysisService.createMainSentence(article);

			// add to the store
			CollectionService.add(article);
		}
		
		for (Entry<Integer, List<Article>> entry : CollectionService.get().entrySet()) {
			for (Article curArticle : entry.getValue()) {

				LOG.debug("{}", curArticle.getTitle());
				LOG.debug("{}", curArticle.getKeyWordList());

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
