/*
 * @(#)ContentsAnalysisService.java $version 2013. 10. 25.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infoc.domain.Article;
import com.infoc.domain.SentenceInfo;
import com.infoc.util.ParseTest;

/**
 * @author NBP
 */
public class ContentsAnalysisService {
	private static final Logger LOG = LoggerFactory.getLogger(ContentsAnalysisService.class);

	private static final String TITLE_SPLIT_PATTERN = "\\s|\\,|\\[|\\]|\\;|\\'|\\·|\\…|\\!|\\\"|\\“|\\”|\\.\\.";
	public static Splitter TITLE_SPLITTER = Splitter.onPattern(TITLE_SPLIT_PATTERN).trimResults().omitEmptyStrings();

	public static void createMainSentence(Article article) {
		// create key words first
		Set<String> keyWordList = createKeyWorkList(article.getTitle());
		article.setKeyWordList(keyWordList);

		// create key sentences
		List<SentenceInfo> sentenceList = createSentenceList(keyWordList, article.getContents());
		List<SentenceInfo> keySentenceList = createKeySentenceList(sentenceList);

		StringBuilder sb = new StringBuilder();
		for (SentenceInfo sentence : keySentenceList) {
			sb.append(sentence.getSentance()).append(". ");
		}

		article.setMainContents(sb.toString());
	}

	public static Set<String> createKeyWorkList(String title) {
		Set<String> keyWordList = new HashSet<>();
		if (Strings.isNullOrEmpty(title)) {
			return keyWordList;
		}

		// eliminate special characters from title and split it
		Set<String> titleList = Sets.newHashSet(
			TITLE_SPLITTER
				.omitEmptyStrings()
				.trimResults()
				.split(
					title.replaceAll("[^\\p{L}\\p{Z}]", " ").replaceAll("\\[.*\\]", "")
				)
			);

		for (String word : titleList) {
			if (word.length() > 1 && !isSpecialChar(word)) {
				keyWordList.add(word);
			}
		}

		return keyWordList;
	}

	/**
	 * TODO: 테스트 필요
	 */
	public static boolean isSpecialChar(String str) {
		char c;
		int cint;
		for (int n = 0; n < str.length(); n++) {
			c = str.charAt(n);
			cint = (int)c;
			if (cint < 48 || (cint > 57 && cint < 65)
				|| (cint > 90 && cint < 97) || cint > 122) {
				return false;
			}
		}

		return true;
	}

	public static List<SentenceInfo> createSentenceList(Set<String> keyWordList, String contents) {
		List<SentenceInfo> sentenceList = new ArrayList<>();

		List<String> sList = Lists.newArrayList(
			Splitter.onPattern("\\.\\s")
				.trimResults()
				.omitEmptyStrings()
				.split(contents)
			);

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

	public static List<SentenceInfo> createKeySentenceList(List<SentenceInfo> sentenceList) {
		List<SentenceInfo> keySentenceList = new ArrayList<>();

		List<SentenceInfo> matchedOrderList = Article.matchedOrder.nullsLast().reverse().sortedCopy(sentenceList);
		int maxKeySentence = matchedOrderList.size() <= 3 ? matchedOrderList.size() : 3;

		for (int i = 0; i < maxKeySentence; i++) {
			keySentenceList.add(matchedOrderList.get(i));
		}

		Collections.sort(keySentenceList, Article.indexOrder.nullsFirst());

		return keySentenceList;
	}

	public static void clearInvalidWords(Article article) {
		// ". "를 기준으로 문장을 자르는데 "다.XX"인 경우가 있어 미리 변경해준다..ㅜㅜ
		String desc = article.getContents().replaceAll("&nbsp;", "").replaceAll("다\\.", "다\\. ");

		StringBuffer sb = new StringBuffer();
		boolean appendFlag = true;
		for (int i = 0; i < desc.length(); i++) {
			char c = desc.charAt(i);
			if (c == '【' || c == '[' || c == '(' || c == '<') {
				appendFlag = false;
				continue;
			}
			if (c == '】' || c == ']' || c == ')' || c == '>') {
				appendFlag = true;
				continue;
			}

			if (appendFlag) {
				sb.append(c);
			}
		}

		article.setContents(sb.toString());
	}
}
