package com.infoc.crawler;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.infoc.domain.Article;
import com.infoc.service.USCollectionService;
import com.infoc.service.USContentsAnalysisService;

public class NYTimesNewsCrawlerTest {
	private static final Logger LOG = LoggerFactory
			.getLogger(NYTimesNewsCrawlerTest.class);

	@Test
	public void test2() {
		String t = "Party’s New Leader Agrees to Form Government in Italy";

		LOG.debug("{}", t.replaceAll("[^\\p{L}\\p{Z}]", ""));

		Set<String> titleList = Sets
				.newHashSet(USContentsAnalysisService.TITLE_SPLITTER
						.omitEmptyStrings().trimResults()
						.split(t.replaceAll("[^\\p{L}\\p{Z}]", " ")));
		LOG.debug("{}", titleList);

		for (String word : titleList) {
			if (word.length() > 2 && StringUtils.isAlphanumeric(word)) {
				LOG.debug("{}", word);
			}
		}
	}
	
	private static String TODAY = "http://www.nytimes.com/services/xml/rss/nyt/HomePage.xml";

	@Test
	public void test1() {
		NYTimesNewsCrawler d = new NYTimesNewsCrawler();
		d.createArticlList();
	
		LOG.debug("{}, {}",  USCollectionService.TODAY_CACHE.isEmpty(),  USCollectionService.TODAY_CACHE.size());
	}

	
	@Test
	public void testGetImg() throws IOException {
		Document doc = Jsoup.connect("http://www.nytimes.com/2014/02/18/nyregion/in-brooklyn-rejecting-the-monotony-of-the-glass-and-steel-look.html?partner=rss&emc=rss").timeout(6000).get();
		Elements contentsArea = doc.select("#story");
		
		// parse img url
		LOG.debug("{}", contentsArea.select(".image > img").attr("src"));
	}
	
	@Test
	public void testPreposition() throws IOException {
		String word = "unlike";
		if(word.matches("the|aboard|about|above|across|after|against"
				+ "|along|amid|among|anti|around|before|behind|below|beneath"
				+ "|beside|besides|between|beyond|but|despite|down|during|except"
				+ "|excepting|excluding|following|for|from|inside|into|like|near"
				+ "|off|onto|opposite|outside|over|past|per|plus|regarding|round"
				+ "|since|than|through|toward|under|underneath|until|up|upon|unlike"
				+ "|versus|via|with|within|without")) {
			
			LOG.debug("haha");
		}
	}
}
