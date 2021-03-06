/*
 * @(#)DaumNewsCrawler.java $version 2013. 10. 25.
 */

package com.infoc.crawler.kr;

import com.google.common.base.Strings;
import com.infoc.crawler.NewsCrawler;
import com.infoc.domain.Article;
import com.infoc.enumeration.ArticleSection;
import com.infoc.service.CollectionService;
import com.infoc.service.ContentsAnalysisService;
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
public class OtherNewsCrawler implements NewsCrawler {
    private static final Logger LOG = LoggerFactory.getLogger(OtherNewsCrawler.class);

    private static String NEWSTAPA = "http://www.newstapa.com/feed";
    private static String NEWSPEPPER = "http://newspeppermint.com/feed";
    private static String CLIEN_NEWS1 = "http://feeds.feedburner.com/c_news";
    private static String CLIEN_NEWS2 = "http://feeds.feedburner.com/c_use";
    private static String CLIEN_NEWS3 = "http://feeds.feedburner.com/c_shop";
    private static String SLOW_NEWS = "http://feeds.feedburner.com/slownews";
    private static String PPSS = "http://feeds.feedburner.com/ppss";

    private List<Article> articleList = new ArrayList<>();

    @Autowired
    public CollectionService collectionService;

    @Override
    public List<Article> createArticleList() {
        LOG.debug("get RSS from USER.");

        // 403 Forbidden from the out side of korea...
        // createListBySection(LIKELINK, ArticleSection.OTHERS);

        createListBySection(NEWSTAPA, ArticleSection.TODAY);
        createListBySection(NEWSPEPPER, ArticleSection.TODAY);
        createListBySection(SLOW_NEWS, ArticleSection.TODAY);
        createListBySection(PPSS, ArticleSection.TODAY);
        createListBySection(CLIEN_NEWS1, ArticleSection.TODAY);
        createListBySection(CLIEN_NEWS2, ArticleSection.TODAY);
        createListBySection(CLIEN_NEWS3, ArticleSection.TODAY);

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

            if (article.getContents().length() < 100) {
                continue;
            }

            DateTime currTime = new DateTime(DateTimeZone.forID("Asia/Seoul"));
            if (article.getPubDate().before(currTime.minusDays(1).toDate())) {
                return;
            }

            // create the main contents
            ContentsAnalysisService.createMainSentence(article);

            // add to the store
            collectionService.add(article);
        }
    }

    private Article parseRSSItem(SyndEntry rssItem, ArticleSection section) {
        Article article = new Article();
        article.setSection(section);
        article.setAuthor(rssItem.getAuthor());

        if (rssItem.getLink().contains("likelink")) {
            article.setLink(findLikeLink(rssItem.getLink()));
        } else {
            article.setLink(rssItem.getLink());
        }

        article.setCountry("KR");

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

    private String findLikeLink(String likeUrl) {
        return "http://likelink.co.kr/" + likeUrl.substring(likeUrl.lastIndexOf("/") + 1);
    }

    private void parseContentsFromLink(SyndEntry rssItem, Article article) {
        String rssLink = rssItem.getLink();

        Document doc;
        try {
            doc = Jsoup.connect(rssLink)
                       .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                       .timeout(6000)
                       .get();
        } catch (IOException e) {
            LOG.error(rssLink + "\n", e);
            return;
        }

        if (rssLink.contains("likelink")) {
            article.setLink(doc.select("iframe").attr("src"));
            article.setContents(Jsoup.parse(rssItem.getDescription().getValue()).select("ul").text());
            return;
        }

        Elements contentsArea;
        if (rssLink.contains("newspeppermint")) {

            contentsArea = doc.select(".entry");

        } else if (rssLink.contains("clien")) {

            contentsArea = doc.select("#writeContents");

        } else if (rssLink.contains("slownews")) {

            contentsArea = doc.select("#article_content");

            // extract the img link
            article.setImg(contentsArea.select("img").attr("src"));
        } else if (rssLink.contains("newstapa")) {

            contentsArea = doc.select(".entry-content");

            // extract the img link
            article.setImg(contentsArea.select("img").attr("src"));
        } else if (rssLink.contains("ppss")) {

            contentsArea = doc.select(".tha-content");

            // extract the img link
            article.setImg(contentsArea.select("img").attr("src"));
        } else if (rssLink.contains("mediatoday")) {

            contentsArea = doc.select("#media_body");

            // extract the img link
            article.setImg(contentsArea.select("img").attr("src"));
        } else {
            return;
        }

        article.setContents(ContentsAnalysisService.removeInvalidWordsForKR(contentsArea.text().trim()));
    }

}
