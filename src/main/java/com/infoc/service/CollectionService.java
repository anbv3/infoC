package com.infoc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

import com.infoc.domain.Article;
import org.joda.time.DateTime;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

public class CollectionService {
	public static Map<Integer, List<Article>> CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections.reverseOrder());

	static {
		for (int i = 0; i < 24; i++) {
			CACHE.put(i, new ArrayList<Article>());
		}
	}

	private static final Integer MAX_NUM_IN_HOUR = 9;
	private static final Integer MAX_DUP_NUM = 2;
	private static final String TITLE_SPLIT_PATTERN = "\\s|\\,|\\[|\\]|\\;|\\'|\\·|\\…|\\!|\\\"|\\“|\\”";
	private static Splitter SPLITTER = Splitter.onPattern(TITLE_SPLIT_PATTERN).trimResults().omitEmptyStrings();

	public static boolean isDuplicate(Article curArticle, Article newArticle) {

		// Make the basis keyword list to compare with another title of the article
		Set<String> curKeyWordList = curArticle.getKeyWordList();
		if (curKeyWordList.isEmpty()) {
			curKeyWordList = Sets.newHashSet(SPLITTER.split(curArticle.getTitle()));
		}

		// Compare the basis list with the target's keyword list and compare again backward
		List<String> dupWorkList = new ArrayList<>();
		List<String> clearWorkList = new ArrayList<>();
		for (String oriWord : curKeyWordList) {
			for (String tarWord : Sets.newHashSet(SPLITTER.split(newArticle.getTitle()))) {
				if (oriWord.contains(tarWord) || tarWord.contains(oriWord)) {

                    // store the dup keyword
                    if(oriWord.length() < tarWord.length()) {
                        dupWorkList.add(oriWord);
                        clearWorkList.add(oriWord);
                    } else {
                        dupWorkList.add(tarWord);
                    }
				}
			}
		}

		// determine the new article is duplicated one or not by the number of the dup. keyword list.
		if (dupWorkList.size() >= MAX_DUP_NUM) {

            // update the basis list
			curKeyWordList.remove(clearWorkList);
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
