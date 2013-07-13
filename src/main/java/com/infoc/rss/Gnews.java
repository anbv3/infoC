/*
 * @(#)Gnews.java $version 2013. 7. 13.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.rss;

import com.infoc.domain.Article;
import com.sun.syndication.feed.synd.SyndEntry;

/**
 * @author NBP
 */
public class Gnews {
	/*
<title>여야, '귀태 논란' 일단락…홍준표 고발되나 - 데일리안</title>
<link>
http://news.google.com/news/url?sa=t&fd=R&usg=AFQjCNGfFwHBY32XbTQxcFLVmY06pQAomA&url=http://www.dailian.co.kr/news/view/356455
</link>
<guid isPermaLink="false">tag:news.google.com,2005:cluster=52778469384847</guid>
<category>주요 뉴스</category>
<pubDate>Sat, 13 Jul 2013 07:55:08 GMT</pubDate>
<description>
<table border="0" cellpadding="2" cellspacing="7" style="vertical-align: top;">
	<tr>
		<td width="80" align="center" valign="top"><font
			style="font-size: 85%; font-family: arial, sans-serif"> <a
				href="http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNGfFwHBY32XbTQxcFLVmY06pQAomA&amp;url=http://www.dailian.co.kr/news/view/356455">
					<img src="//t0.gstatic.com/images?q=tbn:ANd9GcQohDODuKtqSNXL4jqnPUUds6IGv38HxF2139EEidKp4UAqCI9UPauvIHE"
					alt="" border="1" width="80" height="80" /><br />
				<font size="-2">데일리안</font>
			</a></font></td>
		<td valign="top" class="j"><font style="font-size: 85%; font-family: arial, sans-serif"><br />
			<div style="padding-top: 0.8em;">
					<img alt="" height="1" width="1" />
				</div>
				<div class="lh">
					<a
						href="http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNGfFwHBY32XbTQxcFLVmY06pQAomA&amp;url=http://www.dailian.co.kr/news/view/356455"><b>여야,
							&#39;귀태 논란&#39; 일단락…홍준표 고발되나</b></a><br />
					<font size="-1"><b><font color="#6f6f6f">데일리안</font></b></font><br />
					<font size="-1">여야가 민주당 홍익표 의원의 이른바 &#39;귀태(鬼胎) 발언&#39; 논란으로 중단됐던 국회 운영을 정상화하기로 13일 합의했다. 홍 의원의
						발언에 새누리당이 강력 반발하면서 빚어졌던 국회 일정 중단은 이틀 만에 일단락됐다. 새누리당 최경환, 민주당 전병헌 원내대표와&nbsp;<b>...</b>
					</font><br />
					<font size="-1"><a
						href="http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNHVn0tOYyB1MKnSNLw6_ZTk59gAXQ&amp;url=http://vip.mk.co.kr/news/view/21/20/1029048.html">`귀태`
							발언 논란 민주당 홍익표 원내대변인 사퇴 (상보)</a><font size="-1" color="#6f6f6f"><nobr>매일경제</nobr></font></font><br />
					<font size="-1"><a
						href="http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNGihWFoj85QlhlOIkeoRp1LwnsVJg&amp;url=http://news.sbs.co.kr/section_news/news_read.jsp?news_id%3DN1001880984">[취재파일]
							국회 세운 새누리…여당 맞나?</a><font size="-1" color="#6f6f6f"><nobr>SBS뉴스</nobr></font></font><br />
					<font size="-1"><a
						href="http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNHxLYCUWxMBPYK3LfzT6_k19eEzhA&amp;url=http://www.kookje.co.kr/news2011/asp/newsbody.asp?code%3D0100%26key%3D20130713.99002134127">홍익표
							&#39;귀태&#39; 발언 궁금해? &#39;귀태&#39;발언 . 브리핑 전문 어떻길래?</a><font size="-1" color="#6f6f6f"><nobr>국제신문</nobr></font></font><br />
					<font size="-1" class="p"><a
						href="http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNFg5-hsrmi1EpTXiMIzYgnKVzAPnw&amp;url=http://www.sisainlive.com/news/articleView.html?idxno%3D17138"><nobr>시사IN</nobr></a>&nbsp;-<a
						href="http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNFCeQ7bSBe-jiLlKwecFA-2gtl5iQ&amp;url=http://imnews.imbc.com/news/2013/politic/article/3309487_11199.html"><nobr>MBC뉴스</nobr></a>&nbsp;-<a
						href="http://news.google.com/news/url?sa=t&amp;fd=R&amp;usg=AFQjCNF48sP4lwFxRW3Fdp_w0JE5Wu4Kxg&amp;url=http://news.chosun.com/site/data/html_dir/2013/07/13/2013071300189.html"><nobr>조선일보</nobr></a></font><br />
					<font class="p" size="-1"><a class="p"
						href="http://news.google.co.kr/news/story?ncl=dnA90Yq-BBRtCdMuJicQVv1CSB2jM&amp;ned=kr&amp;topic=h"><nobr>
								<b>전체뉴스 334개&nbsp;&raquo;</b>
							</nobr></a></font>
				</div></font></td>
	</tr>
</table>
</description>
	 */

	public Article parseItem(SyndEntry rssItem) {
		Article article = new Article();
		
		article.setPubDate(rssItem.getPublishedDate());
		article.setContents(rssItem.getDescription().getValue());
		
		parseTitleAuthor(rssItem.getTitle(), article);
		parseLink(rssItem.getLink(), article);
		parseDescrption(rssItem.getDescription().getValue(), article);
		
		return article;
	}
	
	public void parseTitleAuthor(String title, Article article) {
		int idx = title.lastIndexOf("-");
		article.setTitle(title.substring(0, idx).trim());
		article.setAuthor(title.substring(idx+1, title.length()).trim());
	}
	
	public void parseLink(String link, Article article) {
		int idx = link.lastIndexOf("http");
		article.setLink(link.substring(idx+7));
	}
	
	public void parseDescrption(String desc, Article article) {
		StringBuffer sb = new StringBuffer();
		
		boolean appendFlag = false;
		for (int i = 0; i < desc.length(); i++){
		    char c = desc.charAt(i);   
		    if(c == '<') {
		    	appendFlag = false;
		    	continue;
		    }
		    if(c == '>') {
		    	appendFlag = true;
		    	continue;
		    }
		    
		    if(appendFlag) {
		    	sb.append(c);
		    }
		}
		
		article.setContents(sb.toString());
	}
}
