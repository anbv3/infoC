/*
 * @(#)ContentsAnalysisService.java $version 2013. 10. 25.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infoc.domain.Article;
import com.infoc.domain.SentenceInfo;

/**
 * @author NBP
 */
public class ContentsAnalysisService {

	public void createMainSentence(Article article) {
		List<SentenceInfo> sentenceList = createSentenceList(article.getKeyWordList(), article.getContents());
		List<SentenceInfo> keySentenceList = createKeySentenceList(sentenceList);

		StringBuilder sb = new StringBuilder();
		for (SentenceInfo sentence : keySentenceList) {
			sb.append(sentence.getSentance()).append(". ");
		}

		article.setMainContents(sb.toString());
	}

	private List<SentenceInfo> createSentenceList(Set<String> keyWordList, String contents) {
		List<SentenceInfo> sentenceList = new ArrayList<>();

		List<String> sList = Lists.newArrayList(Splitter.onPattern("\\.\\s").trimResults()
			.omitEmptyStrings().split(contents));

		for (int i = 0; i < sList.size(); i++) {
			String sentence = sList.get(i);

			SentenceInfo scInfo = new SentenceInfo();
			scInfo.setIndex(i);
			scInfo.setLength(sentence.length());
			scInfo.setSentance(sentence);
			scInfo.checkKeyword(keyWordList);

			sentenceList.add(scInfo);
		}

		return sentenceList;
	}

	private List<SentenceInfo> createKeySentenceList(List<SentenceInfo> sentenceList) {
		List<SentenceInfo> keySentenceList = new ArrayList<>();

		List<SentenceInfo> matchedOrderList = Article.matchedOrder.nullsLast().reverse().sortedCopy(sentenceList);
		int maxKeySentence = matchedOrderList.size() <= 3 ? matchedOrderList.size() : 3;

		for (int i = 0; i < maxKeySentence; i++) {
			keySentenceList.add(matchedOrderList.get(i));
		}

		Collections.sort(keySentenceList, Article.indexOrder.nullsFirst());

		return keySentenceList;
	}
}
