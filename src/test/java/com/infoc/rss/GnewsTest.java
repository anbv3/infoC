package com.infoc.rss;

import org.joda.time.DateTime;
import org.junit.Test;

import com.infoc.domain.Article;

public class GnewsTest {
	
	private String DESC = "<table style=\"vertical-align: top;\">" +
				"<font size=\"-2\">데일리안</font>" + 
				"&#39;귀태 논란&#39; 일단락…홍준표 고발되나</b></a><br />" +
				"<b>전체뉴스 334개&nbsp;&raquo;</b>" +
				"url=http://www.dailian.co.kr/news/view/356455\"><b> 여야,&#39;귀태 논란&#39;" +
				"일단락…홍준표 고발되나</b></a><br /> <font size=\"-1\"><b><font"+
				"color=\"#6f6f6f\">데일리안</font></b></font><br /> <font size=\"-1\">여야가 민주당 홍익표"+
				"의원의 이른바 &#39;귀태(鬼胎) 발언&#39; 논란으로 중단됐던 국회 운영을 정상화하기로" +
				"새누리당이 강력 반발하면서 빚어졌던 국회 일정 중단은 이틀 만에 일단락됐다. 새누리당"+
				"원내대표와&nbsp;<b>...";
	
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
}
