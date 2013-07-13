/*
 * @(#)Nnews.java $version 2013. 7. 13.
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
public class Nnews {

	/*
		<author>OSEN</author>
		<title>
		<![CDATA[ [사진]황우슬혜, '권민-윤지민 결혼 축하해요~' ]]>
		</title>
		<link>
		http://news.naver.com/main/read.nhn?mode=LSD&mid=sec&sid1=106&oid=109&aid=0002574668
		</link>
		<category>연예</category>
		<description>
		<![CDATA[
		배우 황우슬혜가 13일 오후 서울 역삼동 라움
		]]>
		</description>
		<pubDate>Sat, 13 Jul 13 17:01:00 +0900</pubDate>
	 */
	
	public Article parseItem(SyndEntry rssItem) {
		Article article = new Article();
		article.setAuthor(rssItem.getAuthor());
		article.setContents(rssItem.getDescription().getValue());
		article.setLink(rssItem.getLink());
		article.setPubDate(rssItem.getPublishedDate());
		article.setTitle(rssItem.getTitle());
		
		return null;
	}
	
}
