package com.infoc.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	private Set<String> keyWordList = new HashSet<>();

	private List<SentenceInfo> sentenceList = new ArrayList<>();

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

	public Set<String> getKeyWordList() {
		return keyWordList;
	}

	public void setKeyWordList(Set<String> keyWordList) {
		this.keyWordList = keyWordList;
	}

	public List<SentenceInfo> getSentenceList() {
		return sentenceList;
	}

	public void setSentenceList(List<SentenceInfo> sentenceList) {
		this.sentenceList = sentenceList;
	}

}
