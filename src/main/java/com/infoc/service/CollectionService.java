package com.infoc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import com.infoc.domain.Article;
import com.infoc.rss.DnewsTest;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

public class CollectionService {
	private static final Logger LOG = LoggerFactory.getLogger(CollectionService.class);

	public static Map<Integer, List<Article>> CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections.reverseOrder());

	static {
		for (int i = 0; i < 24; i++) {
			CACHE.put(i, new ArrayList<Article>());
		}
	}

	private static final Integer MAX_NUM_IN_HOUR = 9;
	private static final Integer MAX_DUP_NUM = 2;
	private static final String TITLE_SPLIT_PATTERN = "\\s|\\,|\\[|\\]|\\;|\\'|\\·|\\…|\\!|\\\"|\\“|\\”|\\.\\.";
	public static Splitter SPLITTER = Splitter.onPattern(TITLE_SPLIT_PATTERN).trimResults().omitEmptyStrings();

	public static boolean isDuplicate(Article curArticle, Article newArticle) {

		Set<String> curKeyWordList = curArticle.getKeyWordList();

		// Compare the basis list with the target's keyword list and compare again backward
		List<String> dupWordList = new ArrayList<>();
		List<String> clearWordList = new ArrayList<>();
		for (String oriWord : curKeyWordList) {
			for (String tarWord : Sets.newHashSet(SPLITTER.split(newArticle.getTitle()))) {

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

		// determine the new article is duplicated one or not by the number of the dup. keyword list.
		if (dupWordList.size() >= MAX_DUP_NUM) {

			// update the basis list
			curKeyWordList.remove(clearWordList);
			curKeyWordList.addAll(dupWordList);
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

					// update the key sentence list of the curArticle
					// 새 문장만 먼저 구현
					isNew = false;
					break;
				}
			}
		}

		if (isNew) {
			int hour = newArticle.getPubDate().getHourOfDay();
			if (CollectionService.CACHE.get(hour).size() < MAX_NUM_IN_HOUR) {
				// find and update the key sentence list in the article
				
				CollectionService.CACHE.get(hour).add(newArticle);
			}
		}
	}

	public static Map<Integer, List<Article>> get() {
		return CACHE;
	}
}
