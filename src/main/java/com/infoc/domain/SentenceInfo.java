package com.infoc.domain;

import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class SentenceInfo {
	private Integer index;
	private String sentance;
	private Integer length;
	private Integer matchedWord; // the number of matched words with the keyword list

	public void checkKeyword(Set<String> keywordList) {
		int matchedCnt = 0;
		if (keywordList.isEmpty()) {
			this.matchedWord = matchedCnt;
		}

		List<String> wList = Lists.newArrayList(
			Splitter.on(" ")
				.trimResults()
				.omitEmptyStrings()
				.trimResults()
				.split(this.sentance)
			);

		for (String word : wList) {
			for (String keyword : keywordList) {
				if (word.contains(keyword) || keyword.contains(word)) {
					matchedCnt++;
				}
			}
		}

		this.matchedWord = matchedCnt;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("index", this.index)
			.add("matchedWord", this.matchedWord)
			.add("sentance", this.sentance)
			.add("length", this.length)
			.toString();
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getMatchedWord() {
		return matchedWord;
	}

	public void setMatchedWord(Integer matchedWord) {
		this.matchedWord = matchedWord;
	}

	public String getSentance() {
		return sentance;
	}

	public void setSentance(String sentance) {
		this.sentance = sentance;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

}
