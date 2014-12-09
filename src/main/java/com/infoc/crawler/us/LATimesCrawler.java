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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Component
public class LATimesCrawler implements NewsCrawler {
    private static final Logger LOG = LoggerFactory.getLogger(LATimesCrawler.class);
    private static String TODAY = "http://www.latimes.com/rss2.0.xml";
    private static String POLITICS = "http://www.latimes.com/local/politics/rss2.0.xml";
    private static String ECON = "http://www.latimes.com/business/rss2.0.xml";
    private static String CULTURE = "http://www.latimes.com/style/rss2.0.xml";
    private static String ENT = "http://www.latimes.com/entertainment/rss2.0.xml";
    private static String SPORT = "http://www.latimes.com/sports/rss2.0.xml";
    private static String IT = "http://www.latimes.com/business/technology/rss2.0.xml";

    private List<Article> articleList = new ArrayList<>();

    @Autowired
    public USCollectionService collectionService;

    @Override
    public List<Article> createArticleList() {
        LOG.debug("get RSS from LA times.");

        createListBySection(TODAY, ArticleSection.TODAY);
        createListBySection(POLITICS, ArticleSection.POLITICS);
        createListBySection(ECON, ArticleSection.ECON);
        createListBySection(CULTURE, ArticleSection.CULTURE);
        createListBySection(ENT, ArticleSection.ENT);
        createListBySection(SPORT, ArticleSection.SPORT);
        createListBySection(IT, ArticleSection.IT);

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
        if (!rssLink.contains("latimes")) {
            return;
        }

        Document doc;
        try {
            // 1. try to get a real url from the rss link
            doc = Jsoup.connect(rssLink).timeout(6000).get();

            Elements contentsArea = doc.getElementsByTag("meta");
            Iterator<Element> itr = contentsArea.iterator();
            String realURL = "";
            while (itr.hasNext()) {
                Element element = itr.next();
                if (element.attr("property").equalsIgnoreCase("og:url")) {
                    realURL = element.attr("content");
                }
            }

            // 2. get the content of this article
            doc = Jsoup.connect(realURL).timeout(6000).get();
        } catch (IOException e) {
            //LOG.error(rssLink + "\n", e);
            return;
        }

        String contentId = ".trb_article_page";
        article.setContents(doc.select(contentId).text());

        // extract the img link ////////////////////////////////////////////////////////
        article.setImg(doc.select(".thumbnail").select("img").attr("src").trim());
    }
}
