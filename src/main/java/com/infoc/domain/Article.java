package com.infoc.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import com.google.common.base.Strings;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.infoc.service.CollectionService;

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

	public void createKeyWorkList() {
		if (Strings.isNullOrEmpty(this.title)) {
			return;
		}
		
		this.keyWordList = Sets.newHashSet(CollectionService.SPLITTER.split(this.title));
		
		
	}

	public static final Ordering<Article> dateOrdering = new Ordering<Article>() {
		@Override
		public int compare(Article left, Article right) {
			return left.getPubDate().compareTo(right.getPubDate());
		}
	};

	private void omitSpecialChar(String str) {
		char c;
		int cint;
		for (int n = 0; n < str.length(); n++) {
			c = str.charAt(n);
			cint = (int)c;
			if (cint < 48 || (cint > 57 && cint < 65) || (cint > 90 && cint < 97) || cint > 122) {
			}
		}
	}

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
		this.title = title.replaceAll("[^\\p{L}\\p{Z}]", "");
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
