package com.infoc.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.infoc.service.CollectionService;

/**
 * @author anbv3
 */
public class Article {
	private static final Logger LOG = LoggerFactory.getLogger(Article.class);
	
	private String hashId;

	private String title;

	private String link;

	private String img;

	private String contents;

	private DateTime pubDate;

	private String author;

	private Set<String> keyWordList = new HashSet<>();

	private List<SentenceInfo> sentenceList = new ArrayList<>();

	private List<SentenceInfo> keySentenceList = new ArrayList<>();

	public static final Ordering<Article> dateOrder = new Ordering<Article>() {
		@Override
		public int compare(Article left, Article right) {
			return left.getPubDate().compareTo(right.getPubDate());
		}
	};

	public static final Ordering<SentenceInfo> matchedOrder = new Ordering<SentenceInfo>() {
		@Override
		public int compare(SentenceInfo left, SentenceInfo right) {
			return left.getMatchedWord().compareTo(right.getMatchedWord());
		}
	};

	public static final Ordering<SentenceInfo> indexOrder = new Ordering<SentenceInfo>() {
		@Override
		public int compare(SentenceInfo left, SentenceInfo right) {
			return left.getIndex().compareTo(right.getIndex());
		}
	};

	public void createKeyWorkList() {
		if (Strings.isNullOrEmpty(this.title)) {
			return;
		}

		Set<String> titleList = Sets.newHashSet(CollectionService.SPLITTER
				.split(this.title));
		for (String word : titleList) {
			if (word.length() > 1 && !isSpecialChar(word)) {
				this.keyWordList.add(word);
			}
		}
	}

	/**
	 * TODO: 테스트 필요
	 */
	private boolean isSpecialChar(String str) {
		char c;
		int cint;
		for (int n = 0; n < str.length(); n++) {
			c = str.charAt(n);
			cint = (int) c;
			if (cint < 48 || (cint > 57 && cint < 65)
					|| (cint > 90 && cint < 97) || cint > 122) {
				return false;
			}
		}

		return true;
	}

	public void createSentenceList() {

		List<String> sList = Lists.newArrayList(Splitter.on(". ").trimResults()
				.omitEmptyStrings().split(getContents()));

		for (int i = 0; i < sList.size(); i++) {
			String sentence = sList.get(i);

			SentenceInfo scInfo = new SentenceInfo();
			scInfo.setIndex(i);
			scInfo.setLength(sentence.length());
			scInfo.setSentance(sentence);
			scInfo.checkKeyword(this.keyWordList);

			this.sentenceList.add(scInfo);
		}
		
		createKeySentenceList();
	}

	public void createKeySentenceList() {

		List<SentenceInfo> matchedOrderList = Article.matchedOrder.nullsLast().reverse().sortedCopy(this.sentenceList);
		int maxKeySentence = matchedOrderList.size() <= 3 ? matchedOrderList.size() : 3; 
		for (int i = 0; i < maxKeySentence; i++) {
			this.keySentenceList.add(matchedOrderList.get(i));
		}
		
		Collections.sort(this.keySentenceList, Article.indexOrder.nullsFirst());
		
		StringBuilder sb = new StringBuilder();
		
		LOG.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(SentenceInfo sentence : this.keySentenceList) {
			LOG.debug(sentence.getSentance());
			sb.append(sentence.getSentance()).append(". ");
			
		}
		LOG.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		this.contents = sb.toString();
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

	public List<SentenceInfo> getKeySentenceList() {
		return keySentenceList;
	}

	public void setKeySentenceList(List<SentenceInfo> keySentenceList) {
		this.keySentenceList = keySentenceList;
	}

}
