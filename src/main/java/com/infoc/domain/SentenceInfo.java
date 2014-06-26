package com.infoc.domain;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Set;

public class SentenceInfo {
	private Integer index;
	private String sentence;
	private Integer length;
	private Integer matchedWord; // the number of matched words with the keyword list

	// check with keyword to count the number of the matched words
	public void checkKeyword(Set<String> keywordList) {
		int matchedCnt = 0;
		if (keywordList.isEmpty()) {
			this.matchedWord = matchedCnt;
		}

		List<String> wList = Lists.newArrayList(
			Splitter.on(" ")
				.omitEmptyStrings()
				.trimResults()
				.split(this.sentence)
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

    /**
     * 본문에서 각 문장의 위치에 따라 추가 점수 부여..ㅋ
     * @param totalNum 본문에서 전체 문장의 갯수
     */
    public void addPositionPnt(int totalNum) {
        float pRatio = (float)index / (float)totalNum;

        if (Float.compare(pRatio, (float) 0.3) <= 0) {
            this.matchedWord += 1;
            return;
        }

        if (Float.compare(pRatio, (float) 0.7) >= 0) {
            this.matchedWord += 2;
            return;
        }
    }

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("index", this.index)
			.add("matchedWord", this.matchedWord)
			.add("sentence", this.sentence)
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

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

}
