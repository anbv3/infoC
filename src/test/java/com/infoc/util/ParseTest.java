package com.infoc.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infoc.crawler.kr.DaumNewsCrawler;
import com.infoc.domain.Article;
import com.infoc.service.CollectionService;
import com.infoc.service.ContentsAnalysisService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map.Entry;

public class ParseTest {
	private static final Logger LOG = LoggerFactory.getLogger(ParseTest.class);
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
	public void testCurrentTime() {
		System.out.println(new DateTime().getHourOfDay());
	}

	@Test
	public void removeSP() {
		String a = "박희순, `관상 기대되요~` [MBN포토]";
		LOG.debug("{}", a.replaceAll("[^\\p{L}\\p{Z}]", ""));
	}

	@Test
	public void parseTitleSP() {
		String t = "[단독] 무상 학자금도 .. 모자라 저리융자 '이중지원'";
		LOG.debug("-{}", t.replaceAll("\\[.*\\]", ""));
	}

	@Test
	public void parseUrl() {
		String t = "http://news.google.com/news/url?sa=t&fd=R&usg=AFQjCNEr-NVDIylz7Bb65Z2eb2ToK2KNOQ&url=http://sports.chosun.com/news/utype.htm?id%3D201401060100040030002282%26ServiceDate%3D20140105";
		LOG.debug("{}", t.replaceAll(".*url=", ""));
	}

	@Test
	public void parseTitle() {

		String TITLE_SPLIT_PATTERN = "\\s|\\,|\\[|\\]|\\;|\\'|\\·|\\…|\\!|\\\"|\\“|\\|”|\\.\\.";
		Splitter SPLITTER = Splitter.onPattern(TITLE_SPLIT_PATTERN).trimResults().omitEmptyStrings();

		String t = "웹보드게임규제안 시행 1주일 “업체 20% 이행안해 행정처분”";
		
		LOG.debug(t.replaceAll("[^\\p{L}\\p{Z}]", ""));
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
		LOG.debug("{}", a.replaceAll("다\\.", "다\\. "));

		a = "[티브이데일리 한예지 기자] 수능 난이도가 지난 9월 ∼17:00, 40분) 순서로 진행된다. [티브이데일리 한예지 기자 news@tvdaily.co.kr/사진=뉴스 화면 캡처]";
		LOG.debug("{}", a.replaceAll("(?i)\\[.*?\\]", ""));
	}

	@Test
	public void parseSentenceList() {
		DaumNewsCrawler d = new DaumNewsCrawler();
		List<Article> list = d.createArticleList();
		for (Article article : list) {
			// create the main contents
			ContentsAnalysisService.createMainSentence(article);

			// add to the store
			//CollectionService.add(article);
		}

		for (Entry<Integer, List<Article>> entry : CollectionService.getToday().entrySet()) {
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
			for (String s : sList) {
				LOG.debug("{}", s);
			}

		} catch (IOException e) {
			LOG.debug("", e);
		}

	}

	@Test
	public void testGetImgFromNaverNews() {
		String uuu = "http://news.naver.com/main/read.nhn?mode=LSD&mid=sec&sid1=104&oid=073&aid=0002367457";

		Document doc;
		try {
			doc = Jsoup.connect(uuu).get();
			Elements newsHeadlines = doc.select("#articleBody");

			String src = newsHeadlines.select("img").attr("src");

			LOG.debug("{}", newsHeadlines.select("img").text());
			LOG.debug("{}", src);

		} catch (IOException e) {
			LOG.debug("", e);
		}

	}

	@Test
	public void testWordLength() {
		LOG.debug("{}", "강원".length());
		LOG.debug("{}", "강원".getBytes().length);
		LOG.debug("{}", "&nbsp;".replaceAll( "([\\ud800-\\udbff\\udc00-\\udfff])", ""));
	}

	@Test
	public void testSplit() {
		for (String sTitle : "강원#@fff".split("#@")) {
			LOG.debug("{}", sTitle);
		}
		
		DateTime dt = new DateTime(DateTimeZone.forID("America/Los_Angeles"));
		LOG.debug("{}", new DateTime(DateTimeZone.forID("America/Los_Angeles")));
		LOG.debug("{}", new DateTime(DateTimeZone.forID("Asia/Seoul")));
		LOG.debug("{}", DateTime.now(DateTimeZone.forID("Asia/Seoul")).toDate());
		
	}
}
