package com.infoc.crawler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infoc.service.USCollectionService;
import com.infoc.service.USContentsAnalysisService;

public class US_NYTimesCrawlerTest {
	private static final Logger LOG = LoggerFactory
			.getLogger(US_NYTimesCrawlerTest.class);

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

	@Test
	public void test1() {
		NewsCrawler d = new US_ChicagoTribuneCrawler();
		d.createArticlList();

		LOG.debug("{}, {}", USCollectionService.TODAY_CACHE.isEmpty(),
				USCollectionService.TODAY_CACHE.size());
	}

	@Test
	public void testGetImg() throws IOException {
		Document doc = Jsoup
				.connect(
						"http://www.nytimes.com/2014/02/18/nyregion/in-brooklyn-rejecting-the-monotony-of-the-glass-and-steel-look.html?partner=rss&emc=rss")
				.timeout(6000).get();
		Elements contentsArea = doc.select("#story");

		Elements bodyArea = contentsArea.select(".story-body-text");
		StringBuilder sb = new StringBuilder();
		for (Element element : bodyArea) {
			sb.append(element.ownText()).append(" ");
		}
		
		LOG.debug("{}", sb.toString());

		// parse img url
		LOG.debug("{}", contentsArea.select(".image > img").attr("src"));
	}

	@Test
	public void testPreposition() throws IOException {
		String word = "unlike";
		if (word.matches("the|aboard|about|above|across|after|against"
				+ "|along|amid|among|anti|around|before|behind|below|beneath"
				+ "|beside|besides|between|beyond|but|despite|down|during|except"
				+ "|excepting|excluding|following|for|from|inside|into|like|near"
				+ "|off|onto|opposite|outside|over|past|per|plus|regarding|round"
				+ "|since|than|through|toward|under|underneath|until|up|upon|unlike"
				+ "|versus|via|with|within|without")) {

			LOG.debug("haha");
		}
	}

	@Test
	public void testBostonNews() throws IOException {
		Document doc = Jsoup
				.connect(
						"http://www.boston.com/sports/other-sports/olympics/2014/02/17/costas-returns-olympic-coverage/j56d3jKddYFMgQmsRIycIN/story.html?rss_id=Top+Stories")
				.timeout(6000).get();
		Elements contentsArea = doc.select(".blogText");

		// parse img url
		LOG.debug("{}", Strings.isNullOrEmpty(contentsArea.text()));
	}

	@Test
	public void testSpliteSentence() throws UnsupportedEncodingException {
		String kr = "Europe Party’s New Leader Agrees to Form Government in Italy By ELISABETTA POVOLEDOFEB. "
				+ "But his first challenge is to form a coalition that will back his party? "
				+ "which does not have a majority in either house of Parliament. Renzi said that "
				+ "forming a government would “take a few days.\" given the scope of the changes he hoped to enact..";

		List<String> sList = Lists.newArrayList(Splitter
				.onPattern("(?<=\\.\\s)|(?<=\\?\\s)|(?<=\\.\\\")")
				.trimResults().omitEmptyStrings().split(kr));

		for (String s : sList) {
			LOG.debug("{}", s);

		}
	}

}
