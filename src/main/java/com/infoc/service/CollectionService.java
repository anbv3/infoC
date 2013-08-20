package com.infoc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import org.joda.time.DateTime;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.infoc.domain.Article;

public class CollectionService {
	public static Map<Integer, List<Article>> CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(
		Collections.reverseOrder());

	static {
		for (int i = 0; i < 24; i++) {
			CACHE.put(i, new ArrayList<Article>());
		}
	}

	private static Integer MAX_NUM_IN_HOUR = 9;
	private static Integer MAX_DUP_NUM = 2;
	private static String DUP_PATTERN = "\\s|\\,|\\[|\\]|\\;|\\'|\\·|\\…|\\!|\\\"|\\“|\\”";
	private static Splitter SPLITTER = Splitter.onPattern(DUP_PATTERN)
		.trimResults().omitEmptyStrings();

	public static boolean isDuplicate(Article curArticle, Article newArticle) {

		Set<String> curKeyWordList = curArticle.getKeyWordList();
		if (curKeyWordList.isEmpty()) {
			curKeyWordList = Sets.newHashSet(SPLITTER.split(curArticle.getTitle()));
		}

		List<String> dupWorkList = new ArrayList<>();
		for (String oriWord : curKeyWordList) {
			for (String tarWord : Sets.newHashSet(SPLITTER.split(newArticle.getTitle()))) {
				if (oriWord.contains(tarWord) || tarWord.contains(oriWord)) {
					String keyWord = oriWord.length() < tarWord.length() ? oriWord : tarWord;
					dupWorkList.add(keyWord);
				}
			}
		}

		if (dupWorkList.size() >= MAX_DUP_NUM) {
			curKeyWordList.addAll(dupWorkList);
			return true;
		}

		return false;
	}

	public static void add(Article newArticle) {
		// skip the old article...
		if (newArticle.getPubDate().isBefore(DateTime.now().minusDays(1))) {
			return;
		}

		// check the duplicated articles from the stored article.
		boolean isNew = true;
		for (Entry<Integer, List<Article>> entry : CACHE.entrySet()) {
			for (Article curArticle : entry.getValue()) {
				if (isDuplicate(curArticle, newArticle)) {
					isNew = false;
					break;
				}
			}
		}

		if (isNew) {
			int hour = newArticle.getPubDate().getHourOfDay();
			if (CollectionService.CACHE.get(hour).size() < MAX_NUM_IN_HOUR) {
				CollectionService.CACHE.get(hour).add(newArticle);
			}
		}
	}

	public static Map<Integer, List<Article>> get() {
		return CACHE;
	}
}
