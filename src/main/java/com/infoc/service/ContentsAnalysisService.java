package com.infoc.service;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.infoc.domain.Article;
import com.infoc.domain.SentenceInfo;
import com.infoc.util.TopicModeler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContentsAnalysisService {
    private static final Logger LOG = LoggerFactory.getLogger(ContentsAnalysisService.class);

    private static final int MAX_CONTENTS_LENGTH = 400;
    private static final String TITLE_SPLIT_PATTERN = "\\s|\\,|\\[|\\]|\\;|\\'|\\·|\\…|\\!|\\\"|\\“|\\”|\\.\\.";
    public static Splitter TITLE_SPLITTER = Splitter.onPattern(TITLE_SPLIT_PATTERN).trimResults().omitEmptyStrings();

    public static void createMainSentence(Article article) {
        // 키워드 업데이트후 다시 요약문장을 만들어야 하는데 필요없을수도...
        if (!Strings.isNullOrEmpty(article.getMainContents())) {
            return;
        }

        // create key words first
        Set<String> keyWordList = createKeyWorkList(article);
        article.setKeyWordList(keyWordList);

        // title 기반 keyword와 LDA 기반 keyword를 따로 분리해서
        // createSentenceList에 넣고 matched point를 계산한다.

        // create key sentences
        List<SentenceInfo> sentenceList = createSentenceList(keyWordList, article.getContents());
        List<SentenceInfo> keySentenceList = createKeySentenceList(sentenceList);

        StringBuilder sb = new StringBuilder();
        for (SentenceInfo sentence : keySentenceList) {
            sb.append(sentence.getSentence().trim()).append(" ");
        }

        article.setMainContents(sb.toString());
    }

    private static Set<String> createKeyWorkList(Article article) {
        Set<String> keyWordList = new HashSet<>();

        StringBuilder sb = new StringBuilder(article.getTitle());
        sb.append(" ").append(article.getContents());

        try {
            Set<String> topicKeywords = TopicModeler.getMainTopics(sb.toString());
            for (String word : topicKeywords) {
                if (!word.contains("@")) {
                    keyWordList.add(word);
                }
            }

            // eliminate special characters from title and split it
            Set<String> titleList = Sets.newHashSet(TITLE_SPLITTER.omitEmptyStrings()
                                                                  .trimResults()
                                                                  .split(article.getTitle().replaceAll("[^\\p{L}\\p{Z}]", " ")));
            for (String word : titleList) {
                if (word.length() > 1) {
                    keyWordList.add(word);
                }
            }

            LOG.debug("{} => {}", article.getSection().getSection(), keyWordList);
        } catch (IOException e) {
            LOG.debug("", e);
        }

        return keyWordList;
    }

    private static List<SentenceInfo> createSentenceList(Set<String> keyWordList, String contents) {
        List<SentenceInfo> sentenceList = new ArrayList<>();

        List<String> sList = Lists.newArrayList(Splitter.onPattern("(?<=\\.\\s)|(?<=\\?\\s)|(?<=\\. )")
                                                        .trimResults()
                                                        .omitEmptyStrings()
                                                        .split(contents)
        );

        for (int i = 0; i < sList.size(); i++) {
            String sentence = sList.get(i);

            SentenceInfo scInfo = new SentenceInfo();
            scInfo.setIndex(i);
            scInfo.setLength(sentence.length());
            scInfo.setSentence(sentence);

            // 각 문장의 점수 부여
            scInfo.checkKeyword(keyWordList);
            scInfo.addPositionPnt(sList.size());

            sentenceList.add(scInfo);
        }

        return sentenceList;
    }

    private static List<SentenceInfo> createKeySentenceList(List<SentenceInfo> sentenceList) {
        List<SentenceInfo> keySentenceList = new ArrayList<>();

        List<SentenceInfo> matchedOrderList = Article.matchedOrder.nullsLast().reverse().sortedCopy(sentenceList);
        int maxKeySentence = matchedOrderList.size() <= 3 ? matchedOrderList.size() : 3;

        int totalSize = 0;
        for (int i = 0; i < maxKeySentence; i++) {
            keySentenceList.add(matchedOrderList.get(i));
            totalSize += matchedOrderList.get(i).getSentence().length();
            if (totalSize > MAX_CONTENTS_LENGTH) {
                break;
            }
        }

        Collections.sort(keySentenceList, Article.indexOrder.nullsFirst());

        return keySentenceList;
    }

    /**
     * ". "를 기준으로 문장을 자르는데 "다.XX"인 경우가 있어 미리 변경
     * 기타 잡스런 단어 제거
     */
    public static String removeInvalidWordsForKR(String some) {
        if (Strings.isNullOrEmpty(some)) {
            return "";
        }
        return some.replaceAll("&nbsp;", "")
                   .replaceAll("([\\ud800-\\udbff\\udc00-\\udfff])", "")
                   .replaceAll("다\\.", "다\\. ")
                   .replaceAll("(?i)\\【.*?\\】", "")
                   .replaceAll("(?i)\\[.*?\\]", "")
                   .replaceAll("(?i)\\<.*?\\>", "")
                   .replaceAll("(?i)\\(.*?\\)", "");
    }

}
