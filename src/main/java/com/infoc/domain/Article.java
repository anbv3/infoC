package com.infoc.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Ordering;
import com.infoc.enumeration.ArticleSection;
import com.infoc.util.TranslationParser;

/**
 * @author anbv3
 */
@Entity
public class Article extends AbstractPersistable<Long> {
	private static final long serialVersionUID = -4609133080342171773L;
	private static final Logger LOG = LoggerFactory.getLogger(Article.class);

	@Column
	private String hashId; // for later, org.apache.commons.codec.digest

	@Column
	private String title;

	@Column
	private String link;

	@Column
	private String img;

	@Lob 
	@Column
	private String contents;

	@Column
	private Date pubDate;

	@Column
	private String author;

	@Column
	private Integer numDups = 0;

	@Column
	private ArticleSection section;

	@OneToMany
	@JoinColumn(name = "simulars")
	private List<Article> simularList = new ArrayList<>();

	// /////////////////////////////////////////////////////////////////////////////

	@Type(type = "serializable")
	@Column
	private Set<String> keyWordList = new HashSet<>(); // use for summarization and duplication check

	@Lob 
	@Column
	private String mainContents;

	@Lob 
	@Column
	private String transedContents;
	// /////////////////////////////////////////////////////////////////////////////

	public static final Ordering<Article> dateOrder = new Ordering<Article>() {
		@Override
		public int compare(Article left, Article right) {
			return left.getPubDate().compareTo(right.getPubDate());
		}
	};

	public static final Ordering<SentenceInfo> matchedOrder = new Ordering<SentenceInfo>() {
		@Override
		public int compare(SentenceInfo left, SentenceInfo right) {
			// 1. determine by match word
			int match = left.getMatchedWord().compareTo(right.getMatchedWord());
			if (match != 0) {
				return match;
			}

			// 2. pick the longer sentence among the same matched sentences
			return left.getLength().compareTo(right.getLength());
		}
	};

	public static final Ordering<SentenceInfo> indexOrder = new Ordering<SentenceInfo>() {
		@Override
		public int compare(SentenceInfo left, SentenceInfo right) {
			return left.getIndex().compareTo(right.getIndex());
		}
	};

	/*
	 * newArticle이 유사 리스트에 포함되어 있는지 비교
	 * newArticle을 유사 리스트에 추가
	 */
	public void addNewSimilarList(Article similarArticle) {
		for (Article article : this.simularList) {
			if (article.getTitle().equalsIgnoreCase(similarArticle.getTitle())) {
				return;
			}
		}

		this.simularList.add(similarArticle);
	}

	public void translateMainContents() {
		if (Strings.isNullOrEmpty(this.transedContents)) {
			this.transedContents = TranslationParser.krToEn(this.mainContents);
		}
	}

	public void translateMainContentsFromEnToKr() {
		if (Strings.isNullOrEmpty(this.transedContents)) {
			this.transedContents = TranslationParser.enToKr(this.mainContents);
		}
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("title", this.title)
			.add("link", this.link)
			.add("pubDate", this.pubDate)
			.add("numDups", this.numDups)
			.add("mainContents", this.mainContents)
			.add("keyWordList", this.keyWordList).toString();
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

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public ArticleSection getSection() {
		return section;
	}

	public void setSection(ArticleSection section) {
		this.section = section;
	}

	public Set<String> getKeyWordList() {
		return keyWordList;
	}

	public void setKeyWordList(Set<String> keyWordList) {
		this.keyWordList = keyWordList;
	}

	public String getMainContents() {
		return mainContents;
	}

	public void setMainContents(String mainContents) {
		this.mainContents = mainContents;
	}

	public Integer getNumDups() {
		return numDups;
	}

	public void setNumDups(Integer numDups) {
		this.numDups = numDups;
	}

	public List<Article> getSimularList() {
		return simularList;
	}

	public void setSimularList(List<Article> simularList) {
		this.simularList = simularList;
	}

	public String getTransedContents() {
		return transedContents;
	}

	public void setTransedContents(String transedContents) {
		this.transedContents = transedContents;
	}

}
