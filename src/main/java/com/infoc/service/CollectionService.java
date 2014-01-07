package com.infoc.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.infoc.domain.Article;

public class CollectionService {
	private static final Logger LOG = LoggerFactory.getLogger(CollectionService.class);
	private static final Integer MAX_DUP_NUM = 2;

	public static Map<String, String> ECON_INFO = new ConcurrentHashMap<String, String>();

	public static List<Map<Integer, List<Article>>> CACHE_LIST = new ArrayList<>();
	public static Map<Integer, List<Article>> TODAY_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections.reverseOrder());
	public static Map<Integer, List<Article>> POLITICS_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections.reverseOrder());
	public static Map<Integer, List<Article>> ECON_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections.reverseOrder());
	public static Map<Integer, List<Article>> SOCIETY_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections.reverseOrder());
	public static Map<Integer, List<Article>> CULTURE_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections.reverseOrder());
	public static Map<Integer, List<Article>> ENT_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections.reverseOrder());
	public static Map<Integer, List<Article>> SPORT_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections.reverseOrder());
	public static Map<Integer, List<Article>> IT_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections.reverseOrder());

	static {
		CACHE_LIST.add(TODAY_CACHE);
		CACHE_LIST.add(POLITICS_CACHE);
		CACHE_LIST.add(ECON_CACHE);
		CACHE_LIST.add(SOCIETY_CACHE);
		CACHE_LIST.add(CULTURE_CACHE);
		CACHE_LIST.add(ENT_CACHE);
		CACHE_LIST.add(SPORT_CACHE);
		CACHE_LIST.add(IT_CACHE);

		for (Map<Integer, List<Article>> cache : CACHE_LIST) {
			for (int i = 0; i < 24; i++) {
				cache.put(i, new ArrayList<Article>());
			}
		}
	}

	public static boolean isDuplicate(Article curArticle, Article newArticle) {
		
		if(curArticle.getLink().equalsIgnoreCase(newArticle.getLink())) {
			return true;
		}
		
		
		Set<String> curKeyWordList = curArticle.getKeyWordList();
		Set<String> newKeyWordList = newArticle.getKeyWordList();

		// Compare the basis list with the target's keyword list and compare backward again
		List<String> dupWordList = new ArrayList<>();
		List<String> clearWordList = new ArrayList<>();
		for (String currWord : curKeyWordList) {
			for (String tarWord : newKeyWordList) {

				if (currWord.length() <= 1 || tarWord.length() <= 1) {
					continue;
				}

				if (currWord.contains(tarWord) || tarWord.contains(currWord)) {

					// store the dup keyword
					if (currWord.length() < tarWord.length()) {
						dupWordList.add(currWord);
						clearWordList.add(currWord);
					} else {
						dupWordList.add(tarWord);
					}
				}
			}
		}

		// determine the new article is duplicated or not by the number of the
		// dup. keyword list.
		if (dupWordList.size() >= MAX_DUP_NUM) {

			// update the keyword list
			curKeyWordList.remove(clearWordList);
			curKeyWordList.addAll(dupWordList);
			curArticle.setNumDups(curArticle.getNumDups() + 1);
			
			LOG.debug("curArticle: {}, newArticle:{}", curArticle.getTitle(), newArticle.getTitle());
			LOG.debug("# of dups: {}", curArticle.getNumDups());
			return true;
		}

		return false;
	}

	public static void add(Article newArticle) {
		// skip the old article before today
		if (newArticle.getPubDate().isBefore(DateTime.now(DateTimeZone.forID("Asia/Seoul")).minusDays(1))) {
			return;
		}
		
		// just in case, # of keyword is one, then skip to add
		if (newArticle.getKeyWordList().size() < 2) {
			return;
		}

		// get cache for each type
		Map<Integer, List<Article>> cache = null;
		switch (newArticle.getSection()) {
		case TODAY:
			cache = TODAY_CACHE;
			break;
		case POLITICS:
			cache = POLITICS_CACHE;
			break;
		case ECON:
			cache = ECON_CACHE;
			break;
		case SOCIETY:
			cache = SOCIETY_CACHE;
			break;
		case CULTURE:
			cache = CULTURE_CACHE;
			break;
		case ENT:
			cache = ENT_CACHE;
			break;
		case SPORT:
			cache = SPORT_CACHE;
			break;
		case IT:
			cache = IT_CACHE;
			break;
		default:
			return;
		}

		addNew(newArticle, cache);
	}

	private static void addNew(Article newArticle, Map<Integer, List<Article>> cache) {
		// check the duplicated articles from the stored article.
		for (Entry<Integer, List<Article>> entry : cache.entrySet()) {
			for (Article curArticle : entry.getValue()) {
				if (isDuplicate(curArticle, newArticle)) {
					// create the main contents again..?

					return;
				}
			}
		}

		int hour = newArticle.getPubDate().getHourOfDay();
		cache.get(hour).add(newArticle);
		
		/*
		 * TODO: remove the less important article rather than the first one
		if (cache.get(hour).size() <= 8) {
			cache.get(hour).add(newArticle);
		} else {
			// TODO: remove the less important article rather than the first one
			cache.get(hour).remove(0);
			cache.get(hour).add(newArticle);
		}
		*/
	}

	public static void clearYesterDay() {
		for (Map<Integer, List<Article>> cache : CACHE_LIST) {
			for (Entry<Integer, List<Article>> entry : cache.entrySet()) {
				Iterator<Article> article = entry.getValue().iterator();
				while (article.hasNext()) {
					if (article
							.next()
							.getPubDate()
							.isBefore(
									DateTime.now(
											DateTimeZone.forID("Asia/Seoul"))
											.minusDays(1))) {
						article.remove();
					}
				}
			}
		}

	}

	public static Map<Integer, List<Article>> getToday() {
		return TODAY_CACHE;
	}
}
