package com.infoc.rss;

import org.junit.Test;

import com.infoc.domain.Article;

public class GnewsTest {
	
	private String DESC = "<table style=\"vertical-align: top;\">" +
				"<font size=\"-2\">데일리안</font>" + 
				"&#39;귀태 논란&#39; 일단락…홍준표 고발되나</b></a><br />" +
				"<b>전체뉴스 334개&nbsp;&raquo;</b>";
	
	@Test
	public void parse() {
		Gnews gnews = new Gnews();
		
		Article article = new Article();
		gnews.parseDescrption(DESC, article);
		
		System.out.println(article.getContents());
		
	}
}
