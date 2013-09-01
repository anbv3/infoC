package com.infoc.domain;

public class SentenceInfo {
	private Integer index;
	private Integer matchedWord;
	private Integer length;
	private String sentance;

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
