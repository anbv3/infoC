package com.infoc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infoc.domain.Article;

public class CollectionService {
	private static final Logger LOG = LoggerFactory.getLogger(CollectionService.class);

	public static Map<Integer, List<Article>> CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(
			Collections.reverseOrder());

	static {
		for (int i = 0; i < 24; i++) {
			CACHE.put(i, new ArrayList<Article>());
		}
	}

	private static final Integer MAX_NUM_IN_HOUR = 16;
	private static final Integer MAX_DUP_NUM = 2;

	public static boolean isDuplicate(Article curArticle, Article newArticle) {

		Set<String> curKeyWordList = curArticle.getKeyWordList();
		Set<String> newKeyWordList = newArticle.getKeyWordList();

		// Compare the basis list with the target's keyword list and compare
		// again backward
		List<String> dupWordList = new ArrayList<>();
		List<String> clearWordList = new ArrayList<>();
		for (String oriWord : curKeyWordList) {
			for (String tarWord : newKeyWordList) {

				if (oriWord.length() <= 1 || tarWord.length() <= 1) {
					continue;
				}

				if (oriWord.contains(tarWord) || tarWord.contains(oriWord)) {

					// store the dup keyword
					if (oriWord.length() < tarWord.length()) {
						dupWordList.add(oriWord);
						clearWordList.add(oriWord);
					} else {
						dupWordList.add(tarWord);
					}
				}
			}
		}

		// determine the new article is duplicated or not by the number of the dup. keyword list.
		if (dupWordList.size() >= MAX_DUP_NUM) {

			// update the keyword list
			curKeyWordList.remove(clearWordList);
			curKeyWordList.addAll(dupWordList);
			return true;
		}

		return false;
	}

	public static void add(Article newArticle) {
		// skip the old article before today
		if (newArticle.getPubDate().isBefore(DateTime.now().minusDays(1))) {
			return;
		}

		// check the duplicated articles from the stored article.
		for (Entry<Integer, List<Article>> entry : CACHE.entrySet()) {
			for (Article curArticle : entry.getValue()) {
				if (isDuplicate(curArticle, newArticle)) {
					// update the key sentence list of the curArticle
					
					// create the main contents again..?
					
					return;
				}
			}
		}

		int hour = newArticle.getPubDate().getHourOfDay();
		CollectionService.CACHE.get(hour).add(newArticle);
		if (CollectionService.CACHE.get(hour).size() > MAX_NUM_IN_HOUR) {
			// 가장 오래된에 하나 제거
		}
	}

	public static Map<Integer, List<Article>> get() {
		return CACHE;
	}
}
