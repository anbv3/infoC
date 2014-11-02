/*
 * @(#)DaumNewsCrawler.java $version 2013. 10. 25.
 */

package com.infoc.crawler.us;

import com.google.common.base.Strings;
import com.infoc.crawler.NewsCrawler;
import com.infoc.domain.Article;
import com.infoc.enumeration.ArticleSection;
import com.infoc.service.ContentsAnalysisService;
import com.infoc.service.USCollectionService;
import com.infoc.service.USContentsAnalysisService;
import com.infoc.util.RSSCrawler;
import com.sun.syndication.feed.synd.SyndEntry;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BaseballNewsCrawler implements NewsCrawler {
    private static final Logger LOG = LoggerFactory.getLogger(BaseballNewsCrawler.class);

    private static String ESPN_TEX = "http://espn.go.com/blog/feed?blog=dallastexas-rangers";
    private static String ESPN_LA = "http://espn.go.com/blog/feed?blog=los-angelesdodger-report";

    private static String MLB_LA = "http://mlb.mlb.com/partnerxml/gen/news/rss/la.xml";
    private static String MLB_TEX = "http://mlb.mlb.com/partnerxml/gen/news/rss/tex.xml";
    private static String MLB_BAL = "http://mlb.mlb.com/partnerxml/gen/news/rss/bal.xml";


    private List<Article> articleList = new ArrayList<>();

    @Autowired
    public USCollectionService collectionService;

    @Override
    public List<Article> createArticleList() {
        LOG.debug("get RSS from BaseballNews.");

        createListBySection(ESPN_TEX, ArticleSection.SPORT);
        createListBySection(ESPN_LA, ArticleSection.SPORT);

        createListBySection(MLB_LA, ArticleSection.SPORT);
        createListBySection(MLB_TEX, ArticleSection.SPORT);
        createListBySection(MLB_BAL, ArticleSection.SPORT);

        return this.articleList;
    }

    private void createListBySection(String rssUrl, ArticleSection section) {
        for (SyndEntry item : RSSCrawler.getArticleList(rssUrl)) {
            Article article = parseRSSItem(item, section);

            if (article == null) {
                continue;
            }

            if (Strings.isNullOrEmpty(article.getContents())) {
                continue;
            }

            if (article.getContents().length() < 300) {
                continue;
            }

            if (article.getPubDate().before(DateTime.now(DateTimeZone.forID("Asia/Seoul")).minusDays(1).toDate())) {
                return;
            }

            // create the main contents
            USContentsAnalysisService.createMainSentence(article);

            // add to the store
            collectionService.add(article);
        }
    }

    private Article parseRSSItem(SyndEntry rssItem, ArticleSection section) {
        Article article = new Article();
        article.setSection(section);
        article.setAuthor(rssItem.getAuthor());
        article.setLink(rssItem.getLink());
        article.setCountry("US");

        DateTime pubDate = new DateTime(rssItem.getPublishedDate(), DateTimeZone.forID("Asia/Seoul"));
        article.setPubDate(new Date(pubDate.getMillis()));
        article.setPubYear(pubDate.getYear());
        article.setPubMonth(pubDate.getMonthOfYear());
        article.setPubDay(pubDate.getDayOfMonth());
        article.setPubHour(pubDate.getHourOfDay());

        article.setTitle(ContentsAnalysisService.removeInvalidWordsForKR(rssItem.getTitle().trim()));
        if (Strings.isNullOrEmpty(article.getTitle()) || article.getTitle().length() < 5) {
            return null;
        }

        parseContentsFromLink(rssItem, article);

        return article;
    }

    private void parseContentsFromLink(SyndEntry rssItem, Article article) {
        String rssLink = rssItem.getLink();

        String contentId;
        if (rssLink.contains("mlb.com")) {
            contentId = ".article-body";
        } else if (rssLink.contains("espn.com")) {
            contentId = ".mod-content";
        } else {
            return;
        }

        Document doc;
        try {
            doc = Jsoup.connect(rssLink).timeout(6000).get();
        } catch (IOException e) {
            //LOG.error(rssLink + "\n", e);
            return;
        }

        Elements contentsArea = doc.select(contentId);
        article.setContents(contentsArea.text());

        // extract the img link ////////////////////////////////////////////////////////
        article.setImg(contentsArea.select("img").attr("src").trim());
    }
}
