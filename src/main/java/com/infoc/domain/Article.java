package com.infoc.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.collect.Ordering;
import com.infoc.enumeration.ArticleSection;

/**
 * @author anbv3
 */
public class Article {
	private static final Logger LOG = LoggerFactory.getLogger(Article.class);

	private String hashId; // for later, org.apache.commons.codec.digest

	private String title;

	private String link;

	private String img;

	private String contents;

	private DateTime pubDate;

	private String author;
	
	private Integer numDups = 0;
	
	private ArticleSection section;

	private List<Article> simularList = new ArrayList<>();
	
	// /////////////////////////////////////////////////////////////////////////////
	private Set<String> keyWordList = new HashSet<>(); // use for summarization
														// and duplication check

	private String mainContents;
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

	// TODO: 신문사별 본문 및 이미지 링크도 같이 파싱해야 함으로 코드 정리 필요
	public void createContentsFromLink() {
		try {
			String contentId = null;

			// for naver news, remove the last link section from the contents
			if (this.link.contains("naver")) {
				// parse the main contents
				contentId = "#articleBody";

				Document doc = Jsoup.connect(this.link).timeout(6000).get();
				Elements contentsArea = doc.select(contentId);
				
				// 본문영역 뒤에 신문사 기사링크 영역 제거
				Elements linkArea = doc.select(".link_news");
				int linkIdx = contentsArea.text().indexOf(linkArea.text());
				if (linkIdx != -1) {
					this.contents = contentsArea.text().substring(0, linkIdx);
				} else {
					this.contents = contentsArea.text();
				}
				
				// parse img url
				this.img = contentsArea.select("img").attr("src");
				
				return;
			}

			if (this.link.contains("daum")) {

				contentId = "#newsBodyShadow";

			} else if (this.link.contains("interview365")) {

				contentId = "#IDContents";

			} else if (this.link.contains("khan")) {

				contentId = "#_article";

			} else if (this.link.contains("segye")) {

				contentId = "#article_txt";

			} else if (this.link.contains("asiae")) {

				contentId = ".article";

			} else if (this.link.contains("chosun")) {

				contentId = ".par";

			} else if (this.link.contains("dailian")) {

				contentId = "#view_con";

			} else if (this.link.contains("newsen")) {

				contentId = "#CLtag";
				
			} else if (this.link.contains("fnnews")) {
				
				contentId = "#contTxt";
				
			} else if (this.link.contains("kookje")) {
				
				contentId = "#news_textArea";

			} else if (this.link.contains("hankooki")) {

				contentId = "#GS_Content";

			} else if (this.link.contains("ittoday")
				|| this.link.contains("unionpress")
				|| this.link.contains("yonhapnews")
				|| this.link.contains("itimes")
				|| this.link.contains("newsis")) {

				contentId = "#articleBody";

			} else if (this.link.contains("hankyung") || this.link.contains("ahatv")) {

				contentId = "#sstvarticle";

			} else if (this.link.contains("sportsworldi")) {

				contentId = "#article_content";

			} else if (this.link.contains("tvdaily")
				|| this.link.contains("etoday")
				|| this.link.contains("ytn")
				|| this.link.contains("vop")) {

				contentId = "#CmAdContent";

			} else {
				LOG.error("Fail to parsing => Link:{}, ContentId:{}", this.link, contentId);
				return;
			}
			
			Document doc = Jsoup.connect(this.link).timeout(6000).get();
			Elements contentsArea = doc.select(contentId);
			this.contents = contentsArea.text();
		} catch (IOException e) {
			LOG.error(this.link + "\n", e);
		}
	}
	
	/*
	 * newArticle이 유사 리스트에 포함되어 있는지 비교
	 * newArticle을 유사 리스트에 추가
	 */
	public void addNewSimilarList(Article similarArticle) {
		for (Article article : simularList) {
			if (article.getTitle().equalsIgnoreCase(similarArticle.getTitle())) {
				return;
			}
		}
		
		simularList.add(similarArticle);
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
	
}
