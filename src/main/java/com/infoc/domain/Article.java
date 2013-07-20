/*
 * @(#)Article.java $version 2013. 7. 13.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.domain;

import org.joda.time.DateTime;

import com.google.common.collect.Ordering;

/**
 * @author anbv3
 */
public class Article {

	private String hashId;
	
	private String title;
	
	private String link;
	
	private String img;
	
	private String contents;
	
	private DateTime pubDate;
	
	private String author;

	public static final Ordering<Article> dateOrdering = new Ordering<Article>() {
        @Override
        public int compare(Article left, Article right) {
            return left.getPubDate().compareTo(right.getPubDate());
        }
    };
	
	
	public String getHashId() {
		return hashId;
	}

	public void setHashId(String hashId) {
		this.hashId = hashId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public DateTime getPubDate() {
		return pubDate;
	}

	public void setPubDate(DateTime pubDate) {
		this.pubDate = pubDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
}
