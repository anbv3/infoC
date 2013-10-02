package com.infoc.rss;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.infoc.domain.Article;
import com.infoc.domain.SentenceInfo;
import com.infoc.service.CollectionService;

public class GnewsTest {
	private static final Logger LOG = LoggerFactory.getLogger(GnewsTest.class);

	private String DESC = "<table style=\"vertical-align: top;\">" +
		"<font size=\"-2\">데일리안</font>" +
		"&#39;귀태 논란&#39; 일단락…홍준표 고발되나</b></a><br />" +
		"<b>전체뉴스 334개&nbsp;&raquo;</b>" +
		"url=http://www.dailian.co.kr/news/view/356455\"><b> 여야,&#39;귀태 논란&#39;" +
		"일단락…홍준표 고발되나</b></a><br /> <font size=\"-1\"><b><font" +
		"color=\"#6f6f6f\">데일리안</font></b></font><br /> <font size=\"-1\">여야가 민주당 홍익표" +
		"의원의 이른바 &#39;귀태(鬼胎) 발언&#39; 논란으로 중단됐던 국회 운영을 정상화하기로" +
		"새누리당이 강력 반발하면서 빚어졌던 국회 일정 중단은 이틀 만에 일단락됐다. 새누리당" +
		"원내대표와&nbsp;<b>...";

	@Test
	public void ttt() throws UnsupportedEncodingException {
		String aa = ".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*";

		System.err.println("Spain Train Derailment".matches(aa));
		System.err.println("Spain Train가나 Derailment".matches(aa));

		// System.out.println(StringEscapeUtils.unescapeHtml(DESC));
	}

	@Test
	public void parse() {
		Gnews gnews = new Gnews();

		Article article = new Article();
		gnews.parseDescrption(DESC, article);

		System.out.println(article.getContents());

	}

	@Test
	public void testCurrentTime() {
		System.out.println(new DateTime().getHourOfDay());
	}

	@Test
	public void testParseContents() {

		Gnews gnews = new Gnews();
		Article article = new Article();
		gnews.parseDescrption(DESC, article);

		List<String> sList = Lists.newArrayList(Splitter.on(".")
			.trimResults()
			.omitEmptyStrings()
			.trimResults()
			.split(article.getContents()));

		System.out.println(sList);
	}

	@Test
	public void parseContents() {
		Nnews gnews = new Nnews();
		gnews.getNews();

		for (Entry<Integer, List<Article>> entry : CollectionService.CACHE.entrySet()) {
			for (Article curArticle : entry.getValue()) {

				LOG.debug("{}", curArticle.getTitle());
				LOG.debug("{}", curArticle.getKeyWordList());
				LOG.debug("{}", curArticle.getContents());

				for (SentenceInfo sc : curArticle.getSentenceList()) {
					LOG.debug("{}", sc.toString());
				}
				LOG.debug("-----------------------------------------");
				
				for (SentenceInfo sc : curArticle.getKeySentenceList()) {
					LOG.debug("{}", sc.toString());
				}
				LOG.debug("-----------------------------------------");
			}
		}

	}

	@Test
	public void removeSP() {
		String a = "박희순, `관상 기대되요~` [MBN포토]";
		LOG.debug("{}", a.replaceAll("[^\\p{L}\\p{Z}]", ""));
	}
}
