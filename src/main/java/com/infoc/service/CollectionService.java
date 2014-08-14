package com.infoc.service;

import com.google.common.base.Strings;
import com.infoc.common.Constant;
import com.infoc.domain.Article;
import com.infoc.repository.ArticleRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Service
public class CollectionService {
    private static final Logger LOG = LoggerFactory.getLogger(CollectionService.class);
    private static final Integer MAX_DUP_NUM = 5;

    public static Map<String, String> ECON_INFO = new ConcurrentHashMap<String, String>();

    public static List<Map<Integer, List<Article>>> CACHE_LIST = new ArrayList<>();
    public static Map<Integer, List<Article>> TODAY_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections
            .reverseOrder());
    public static Map<Integer, List<Article>> POLITICS_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections
            .reverseOrder());
    public static Map<Integer, List<Article>> ECON_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections
            .reverseOrder());
    public static Map<Integer, List<Article>> SOCIETY_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections
            .reverseOrder());
    public static Map<Integer, List<Article>> CULTURE_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections
            .reverseOrder());
    public static Map<Integer, List<Article>> ENT_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections.reverseOrder());
    public static Map<Integer, List<Article>> SPORT_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections
            .reverseOrder());
    public static Map<Integer, List<Article>> IT_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections.reverseOrder());
    public static Map<Integer, List<Article>> OTHERS_CACHE = new ConcurrentSkipListMap<Integer, List<Article>>(Collections
            .reverseOrder());

    static {
        CACHE_LIST.add(TODAY_CACHE);
        CACHE_LIST.add(POLITICS_CACHE);
        CACHE_LIST.add(ECON_CACHE);
        CACHE_LIST.add(SOCIETY_CACHE);
        CACHE_LIST.add(CULTURE_CACHE);
        CACHE_LIST.add(ENT_CACHE);
        CACHE_LIST.add(SPORT_CACHE);
        CACHE_LIST.add(IT_CACHE);
        CACHE_LIST.add(OTHERS_CACHE);

        for (Map<Integer, List<Article>> cache : CACHE_LIST) {
            for (int i = 0; i < 24; i++) {
                cache.put(i, new ArrayList<Article>());
            }
        }
    }

    @Autowired
    public ArticleRepository articleRepository;

    @Autowired
    public ArticleService articleService;


    public static Map<Integer, List<Article>> getArticlesByCurrentTime(Map<Integer, List<Article>> map) {
        return getArticlesByCurrentTime(map, 0, null);
    }

    public static Map<Integer, List<Article>> getArticlesByPage(Map<Integer, List<Article>> articleMap, int page, String search) {

        Map<Integer, List<Article>> currMap = new LinkedHashMap<>();

        int idx = 0;
        int from = page * Constant.PAGE_SIZE.getVal();
        int to = from + Constant.PAGE_SIZE.getVal();

        for (Entry<Integer, List<Article>> eachTime : articleMap.entrySet()) {
            if (eachTime.getValue().isEmpty()) {
                continue;
            }

            List<Article> searchedArticles = new ArrayList<>();
            if (!Strings.isNullOrEmpty(search)) {
                for (Article article : eachTime.getValue()) {
                    if (article.getMainContents().contains(search)) {
                        searchedArticles.add(article);
                    }
                }

                if (searchedArticles.isEmpty()) {
                    continue;
                }
            }

            if (idx >= from && idx < to) {
                if (!Strings.isNullOrEmpty(search)) {
                    currMap.put(eachTime.getKey(), searchedArticles);
                } else {
                    currMap.put(eachTime.getKey(), eachTime.getValue());
                }
            }

            idx++;
        }

        return currMap;
    }

    public static Map<Integer, List<Article>> getArticlesByCurrentTime(Map<Integer, List<Article>> map, int page, String search) {
        Map<Integer, List<Article>> articleMap = beforeCurrentTime(map);
        return getArticlesByPage(articleMap, page, search);
    }

    public static Map<Integer, List<Article>> beforeCurrentTime(Map<Integer, List<Article>> map) {

        Map<Integer, List<Article>> currMap = new LinkedHashMap<>();

        DateTime currTime = new DateTime(DateTimeZone.forID("Asia/Seoul"));
        int currentHour = currTime.getHourOfDay();
        for (int i = currentHour; i > 0; i--) {
            if (map.get(i).isEmpty()) {
                continue;
            }

            currMap.put(i, map.get(i));
        }

        return currMap;
    }

    public static boolean isDuplicate(Article curArticle, Article newArticle) {
        Set<String> curKeyWordList = curArticle.getKeyWordList();
        Set<String> newKeyWordList = newArticle.getKeyWordList();

        // Compare the basis list with the target's keyword list and compare backward again
        int dupWordCount = 0;
        for (String currWord : curKeyWordList) {
            if (currWord.length() <= 1) {
                continue;
            }

            for (String tarWord : newKeyWordList) {

                if (tarWord.length() <= 1) {
                    continue;
                }

                if (currWord.contains(tarWord) || tarWord.contains(currWord)) {
                    dupWordCount++;
                    continue;
                }
            }
        }

        // check if they are the same articles
        if (curKeyWordList.size() == dupWordCount) {
            return true;
        }

        // determine the new article is duplicated or not by the number of the dup keyword list.
        if (dupWordCount >= MAX_DUP_NUM) {
            LOG.debug("curArticle: {} => {}", curArticle.getTitle(), curArticle.getKeyWordList());
            LOG.debug("newArticle: {} => {}", newArticle.getTitle(), newArticle.getKeyWordList());

            curArticle.addNewSimilarList(newArticle);
            return true;
        }

        return false;
    }

    public void add(Article newArticle) {
        // just in case, # of keyword is one, then skip to add
        if (newArticle.getKeyWordList().size() < 2) {
            return;
        }

        // get cache for each type
        Map<Integer, List<Article>> cacheLocation = null;
        switch (newArticle.getSection()) {
            case TODAY:
                cacheLocation = TODAY_CACHE;
                break;
            case POLITICS:
                cacheLocation = POLITICS_CACHE;
                break;
            case ECON:
                cacheLocation = ECON_CACHE;
                break;
            case SOCIETY:
                cacheLocation = SOCIETY_CACHE;
                break;
            case CULTURE:
                cacheLocation = CULTURE_CACHE;
                break;
            case ENT:
                cacheLocation = ENT_CACHE;
                break;
            case SPORT:
                cacheLocation = SPORT_CACHE;
                break;
            case IT:
                cacheLocation = IT_CACHE;
                break;
            case OTHERS:
                cacheLocation = OTHERS_CACHE;
                break;
            default:
                return;
        }

        addNew(newArticle, cacheLocation);
    }

    private void addNew(Article newArticle, Map<Integer, List<Article>> cache) {
        // check the duplicated articles from the stored article.
        for (Entry<Integer, List<Article>> entry : cache.entrySet()) {
            for (Article curArticle : entry.getValue()) {
                // check if they are the same articles
                if (curArticle.getTitle().equalsIgnoreCase(newArticle.getTitle()) ||
                    curArticle.getLink().equalsIgnoreCase(newArticle.getLink())) {
                	LOG.debug("{}", curArticle.getTitle());
                    return;
                }

                if (isDuplicate(curArticle, newArticle)) {
                    articleService.update(curArticle);
                    return;
                }
            }
        }

        // if it is the new one, then translate the main contents.
        newArticle.translateMainContents();

        // store in DB without contents
        newArticle.setContents("");

        // get the hour of the time for the time section
        int hour = (new DateTime(newArticle.getPubDate(), DateTimeZone.forID("Asia/Seoul"))).getHourOfDay();
        cache.get(hour).add(newArticle);

        Article storedArticle = articleService.add(newArticle);
    }

    public static void clearYesterday() {
        DateTime currentTime = new DateTime(DateTimeZone.forID("Asia/Seoul"));
        LOG.debug("currentTime: {}", currentTime);

        for (Map<Integer, List<Article>> cache : CACHE_LIST) {
            for (Entry<Integer, List<Article>> entry : cache.entrySet()) {
                Iterator<Article> article = entry.getValue().iterator();
                while (article.hasNext()) {
                    DateTime pubTime = new DateTime(article.next().getPubDate(), DateTimeZone.forID("Asia/Seoul"));
                    if (pubTime.isBefore(currentTime.minusDays(1).plusHours(1))) {
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
