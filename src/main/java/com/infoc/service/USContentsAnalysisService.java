package com.infoc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infoc.domain.Article;
import com.infoc.domain.SentenceInfo;

public class USContentsAnalysisService {
	private static final String TITLE_SPLIT_PATTERN = "\\s|\\,|\\[|\\]|\\;|\\'|\\·|\\…|\\!|\\\"|\\“|\\”|\\.\\.";
	public static Splitter TITLE_SPLITTER = Splitter.onPattern(TITLE_SPLIT_PATTERN).trimResults().omitEmptyStrings();
	private static final int MAX_KEY_SENTENCE = 3;
	
	
	public static void createMainSentence(Article article) {
		// 키워드 업데이트후 다시 요약문장을 만들어야 하는데 필요없을수도...
		if (!Strings.isNullOrEmpty(article.getMainContents())) {
			return;
		}
		
		// create key words first
		Set<String> keyWordList = createKeyWorkList(article.getTitle());
		article.setKeyWordList(keyWordList);

		// create key sentences
		List<SentenceInfo> sentenceList = createSentenceList(keyWordList, article.getContents());
		List<SentenceInfo> keySentenceList = createKeySentenceList(sentenceList);

		StringBuilder sb = new StringBuilder();
		for (SentenceInfo sentence : keySentenceList) {
			sb.append(sentence.getSentance());
			
			if (!sentence.getSentance().endsWith(".")) {
				sb.append(". ");
			}
		}

		article.setMainContents(sb.toString());
		article.setContents("");
	}

	private static Set<String> createKeyWorkList(String title) {
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
					title.replaceAll("[^\\p{L}\\p{Z}]", " ")
				)
			);

		for (String word : titleList) {
			if (word.length() > 2 && StringUtils.isAlphanumeric(word)) {
				
				if(word.matches("the|aboard|about|above|across|after|against"
						+ "|along|amid|among|anti|around|before|behind|below|beneath"
						+ "|beside|besides|between|beyond|but|despite|down|during|except"
						+ "|excepting|excluding|following|for|from|inside|into|like|near"
						+ "|off|onto|opposite|outside|over|past|per|plus|regarding|round"
						+ "|since|than|through|toward|under|underneath|until|up|upon|unlike"
						+ "|versus|via|with|within|without")) {
					continue;
				}
				
				keyWordList.add(word);
			}
		}

		return keyWordList;
	}

	private static List<SentenceInfo> createSentenceList(Set<String> keyWordList, String contents) {
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
			// 각 문장에서 keyword 리스트와 비교하고 매칭되는 개수를 저장
			scInfo.checkKeyword(keyWordList);

			sentenceList.add(scInfo);
		}

		return sentenceList;
	}

	private static List<SentenceInfo> createKeySentenceList(List<SentenceInfo> sentenceList) {
		List<SentenceInfo> keySentenceList = new ArrayList<>();

		List<SentenceInfo> matchedOrderList = Article.matchedOrder.nullsLast().reverse().sortedCopy(sentenceList);
		int maxKeySentence = matchedOrderList.size() <= MAX_KEY_SENTENCE ? matchedOrderList.size() : MAX_KEY_SENTENCE;

		for (int i = 0; i < maxKeySentence; i++) {
			keySentenceList.add(matchedOrderList.get(i));
		}

		Collections.sort(keySentenceList, Article.indexOrder.nullsFirst());

		return keySentenceList;
	}

}