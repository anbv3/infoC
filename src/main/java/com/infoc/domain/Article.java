package com.infoc.domain;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aspectj.apache.bcel.generic.IINC;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.collect.Ordering;

/**
 * @author anbv3
 */
public class Article {
	private static final Logger LOG = LoggerFactory.getLogger(Article.class);

	private String hashId; // for later

	private String title;

	private String link;

	private String img;

	private String contents;

	private DateTime pubDate;

	private String author;

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
			return left.getMatchedWord().compareTo(right.getMatchedWord());
		}
	};

	public static final Ordering<SentenceInfo> indexOrder = new Ordering<SentenceInfo>() {
		@Override
		public int compare(SentenceInfo left, SentenceInfo right) {
			return left.getIndex().compareTo(right.getIndex());
		}
	};

	public void createContentsFromLink() {
		try {
			String contentId = null;

			// for naver news, remove the last link section from the contents
			if (this.link.contains("naver")) {
				contentId = "#articleBody";

				Document doc = Jsoup.connect(this.link).get();
				Elements contentsArea = doc.select(contentId);
				Elements linkArea = doc.select(".link_news");

				int linkIdx = contentsArea.text().indexOf(linkArea.text());
				if (linkIdx != -1) {
					this.contents = contentsArea.text().substring(0, linkIdx);
				} else {
					this.contents = contentsArea.text();
				}
				return;
			}

			if (this.link.contains("daum")) {

				contentId = "#newsBodyShadow";

			} else if (this.link.contains("interview365")) {

				contentId = "#IDContents";

			} else if (this.link.contains("ittoday") || this.link.contains("unionpress")) {

				contentId = "#articleBody";

			} else if (this.link.contains("hankyung") || this.link.contains("ahatv")) {

				contentId = "#sstvarticle";

			} else if (this.link.contains("sportsworldi")) {

				contentId = "#article_content";

			} else if (this.link.contains("tvdaily.mk.co.kr")
				|| this.link.contains("etoday.co.kr")
				|| this.link.contains("vop.co.kr")) {

				contentId = "#CmAdContent";

			} else {
				return;
			}

			Document doc = Jsoup.connect(this.link).get();
			Elements contentsArea = doc.select(contentId);
			this.contents = contentsArea.text();

		} catch (IOException e) {
			LOG.error("", e);
		}
	}

	// public void createKeyWorkList() {
	// if (Strings.isNullOrEmpty(this.title)) {
	// return;
	// }
	//
	// // eliminate special characters from title and split it
	// Set<String> titleList = Sets.newHashSet(CollectionService.SPLITTER
	// .split(this.title.replaceAll("[^\\p{L}\\p{Z}]", "")));
	//
	// for (String word : titleList) {
	// if (word.length() > 1 && !isSpecialChar(word)) {
	// this.keyWordList.add(word);
	// }
	// }
	// }
	//
	// /**
	// * TODO: 테스트 필요
	// */
	// private boolean isSpecialChar(String str) {
	// char c;
	// int cint;
	// for (int n = 0; n < str.length(); n++) {
	// c = str.charAt(n);
	// cint = (int) c;
	// if (cint < 48 || (cint > 57 && cint < 65)
	// || (cint > 90 && cint < 97) || cint > 122) {
	// return false;
	// }
	// }
	//
	// return true;
	// }
	//
	// public void extractMainContents() {
	// createSentenceList();
	// createKeySentenceList();
	//
	// StringBuilder sb = new StringBuilder();
	// for(SentenceInfo sentence : this.keySentenceList) {
	//
	// sb.append(sentence.getSentance()).append(". ");
	// }
	//
	// this.contents = sb.toString();
	// }
	//
	// public void createSentenceList() {
	//
	// List<String> sList =
	// Lists.newArrayList(Splitter.onPattern("\\.\\s").trimResults()
	// .omitEmptyStrings().split(getContents()));
	//
	// for (int i = 0; i < sList.size(); i++) {
	// String sentence = sList.get(i);
	//
	// SentenceInfo scInfo = new SentenceInfo();
	// scInfo.setIndex(i);
	// scInfo.setLength(sentence.length());
	// scInfo.setSentance(sentence);
	// scInfo.checkKeyword(this.keyWordList);
	//
	// this.sentenceList.add(scInfo);
	// }
	// }
	//
	// public void createKeySentenceList() {
	//
	// List<SentenceInfo> matchedOrderList =
	// Article.matchedOrder.nullsLast().reverse().sortedCopy(this.sentenceList);
	// int maxKeySentence = matchedOrderList.size() <= 3 ?
	// matchedOrderList.size() : 3;
	//
	// for (int i = 0; i < maxKeySentence; i++) {
	// this.keySentenceList.add(matchedOrderList.get(i));
	// }
	//
	// Collections.sort(this.keySentenceList, Article.indexOrder.nullsFirst());
	// }

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("title", this.title)
			.add("link", this.link).add("pubDate", this.pubDate)
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

	public Set<String> getKeyWordList() {
		return keyWordList;
	}

	public void setKeyWordList(Set<String> keyWordList) {
		this.keyWordList = keyWordList;
	}

	// public List<SentenceInfo> getSentenceList() {
	// return sentenceList;
	// }
	//
	// public void setSentenceList(List<SentenceInfo> sentenceList) {
	// this.sentenceList = sentenceList;
	// }
	//
	// public List<SentenceInfo> getKeySentenceList() {
	// return keySentenceList;
	// }
	//
	// public void setKeySentenceList(List<SentenceInfo> keySentenceList) {
	// this.keySentenceList = keySentenceList;
	// }

	public String getMainContents() {
		return mainContents;
	}

	public void setMainContents(String mainContents) {
		this.mainContents = mainContents;
	}
}
