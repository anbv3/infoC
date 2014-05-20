package com.infoc.domain;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Ordering;
import com.infoc.enumeration.ArticleSection;
import com.infoc.util.TranslationParser;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author anbv3
 */
@Entity(name="article")
public class Article extends AbstractPersistable<Long> {
	private static final long serialVersionUID = -4609133080342171773L;
	private static final Logger LOG = LoggerFactory.getLogger(Article.class);

	@Column
	private String hashId; // for later, org.apache.commons.codec.digest

	@Column(length=1024)
	private String title;

	@Column(length=1024)
	private String link;

	@Column(length=1024)
	private String img;

	@Lob
	@Column(length = 10000)
	private String contents;

	@Index(name = "idx_country")
	@Column(columnDefinition="varchar(10) default 'KR'")
	private String country;
	
	@Index(name = "idx_pub_date")
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date pubDate;
	
	@Index(name = "idx_pub_date_year")
	@Column
	private int pubYear;
	
	@Index(name = "idx_pub_date_month")
	@Column
	private int pubMonth;
	
	@Index(name = "idx_pub_date_day")
	@Column
	private int pubDay;
	
	@Index(name = "idx_pub_date_hour")
	@Column
	private int pubHour;

	@Column
	private String author;

	@Column
	private Integer numDups = 0;

	@Index(name = "idx_section")
	@Enumerated(EnumType.STRING)
	@Column
	private ArticleSection section;

	@Column(length=1024)
	private String similarTitle = "";
	
	@Column(length=4096)
	private String similarSection = "";

	// /////////////////////////////////////////////////////////////////////////////

	@Type(type = "serializable")
	@Column
	private Set<String> keyWordList = new HashSet<>(); // use for summarization and duplication check

	@Lob
	@Column(length = 10000)
	private String mainContents;

	@Lob
	@Column(length = 10000)
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
	 * 
	 * title@link
	 * 
	 * <div class="panel-article-info col-xs-12">
	 * <a href="${sArticle.link}" target="_blank">${sArticle.title}</a>
	 * </div>
	 */
	public void addNewSimilarList(Article similarArticle) {
		if (!Strings.isNullOrEmpty(this.similarTitle)) {
			for (String sTitle : this.similarTitle.split("#@")) {
				if ( sTitle.contains(similarArticle.getTitle()) ) {
					return;
				}
			}
		} else {
            this.similarTitle = "";
        }
		
		// update title
		StringBuilder st = new StringBuilder(this.similarTitle);
		if (!Strings.isNullOrEmpty(this.similarTitle)) {
			st.append("#@");
		}
		st.append(similarArticle.getTitle());
		this.similarTitle = st.toString();

		// TODO: 세련된 방법이 없을까?
		// update section
		StringBuilder ss = new StringBuilder(this.similarSection);
		ss.append("<div class=\"panel-article-info col-xs-12\">");
		ss.append("<a href=\"");
		ss.append(similarArticle.getLink());
		ss.append("\" target=\"_blank\">");
		ss.append(similarArticle.getTitle());
		ss.append("</a></div>");
		this.similarSection = ss.toString();
		
		this.numDups++;
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
			.add("pubYear", this.pubYear)
			.add("pubMonth", this.pubMonth)
			.add("pubMonth", this.pubDay)
			.add("pubHour", this.pubHour)
			.add("numDups", this.numDups)
			.add("mainContents", this.mainContents)
			.add("keyWordList", this.keyWordList)
			.add("similarTitle", this.similarTitle)
			.toString() + "\n";
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getTransedContents() {
		return transedContents;
	}

	public void setTransedContents(String transedContents) {
		this.transedContents = transedContents;
	}

	public int getPubYear() {
		return pubYear;
	}

	public void setPubYear(int pubYear) {
		this.pubYear = pubYear;
	}

	public int getPubMonth() {
		return pubMonth;
	}

	public void setPubMonth(int pubMonth) {
		this.pubMonth = pubMonth;
	}

	public int getPubDay() {
		return pubDay;
	}

	public void setPubDay(int pubDay) {
		this.pubDay = pubDay;
	}

	public int getPubHour() {
		return pubHour;
	}

	public void setPubHour(int pubHour) {
		this.pubHour = pubHour;
	}

	public String getSimilarTitle() {
		return similarTitle;
	}

	public void setSimilarTitle(String similarTitle) {
		this.similarTitle = similarTitle;
	}

	public String getSimilarSection() {
		return similarSection;
	}

	public void setSimilarSection(String similarSection) {
		this.similarSection = similarSection;
	}
	
}
