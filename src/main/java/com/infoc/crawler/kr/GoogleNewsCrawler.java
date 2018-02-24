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
public class GoogleNewsCrawler implements NewsCrawler {
    private static final Logger LOG = LoggerFactory.getLogger(GoogleNewsCrawler.class);

    private static String TODAY = "https://news.google.com/news/rss/?ned=kr&gl=KR&hl=ko"; // 주요기사
    private static String POLITICS = "https://news.google.com/news/rss/headlines/section/topic/POLITICS.ko_kr/%EC%A0%95%EC%B9%98?ned=kr&hl=ko&gl=KR";
    private static String ECON = "https://news.google.com/news/rss/headlines/section/topic/BUSINESS.ko_kr/%EA%B2%BD%EC%A0%9C?ned=kr&hl=ko&gl=KR";
    private static String CULTURE = "https://news.google.com/news/headlines/section/topic/HEALTH.ko_kr/%EA%B1%B4%EA%B0%95?ned=kr&hl=ko&gl=KR";
    private static String ENT = "https://news.google.com/news/rss/headlines/section/topic/ENTERTAINMENT.ko_kr/%EC%97%B0%EC%98%88?ned=kr&hl=ko&gl=KR";
    private static String SPORT = "https://news.google.com/news/rss/headlines/section/topic/SPORTS.ko_kr/%EC%8A%A4%ED%8F%AC%EC%B8%A0?ned=kr&hl=ko&gl=KR";
    private static String IT = "https://news.google.com/news/rss/headlines/section/topic/SCITECH.ko_kr/%EA%B3%BC%ED%95%99%EA%B8%B0%EC%88%A0?ned=kr&hl=ko&gl=KR";
    private static String SOCIETY = "https://news.google.com/news/rss/headlines/section/topic/NATION.ko_kr/%ED%95%9C%EA%B5%AD?ned=kr&hl=ko&gl=KR";

    private List<Article> articleList = new ArrayList<>();

    @Autowired
    public CollectionService collectionService;

    @Override
    public List<Article> createArticleList() {
        LOG.debug("get RSS from Google.");

        createListBySection(TODAY, ArticleSection.TODAY);
//        createListBySection(POLITICS, ArticleSection.POLITICS);
//        createListBySection(ECON, ArticleSection.ECON);
//        createListBySection(SOCIETY, ArticleSection.SOCIETY);
//        createListBySection(CULTURE, ArticleSection.CULTURE);
//        createListBySection(ENT, ArticleSection.ENT);
//        createListBySection(SPORT, ArticleSection.SPORT);
//        createListBySection(IT, ArticleSection.IT);

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

            LOG.debug("{}", article);

            // add to the store
            collectionService.add(article);
        }
    }

    private Article parseRSSItem(SyndEntry rssItem, ArticleSection section) {
        Article article = new Article();
        article.setSection(section);
        article.setLink(rssItem.getLink());
        article.setCountry("KR");

        DateTime pubDate = new DateTime(rssItem.getPublishedDate(), DateTimeZone.forID("Asia/Seoul"));
        article.setPubDate(new Date(pubDate.getMillis()));
        article.setPubYear(pubDate.getYear());
        article.setPubMonth(pubDate.getMonthOfYear());
        article.setPubDay(pubDate.getDayOfMonth());
        article.setPubHour(pubDate.getHourOfDay());

        parseTitleAuthor(rssItem.getTitle(), article);

        // if title is empty or too short, hard to analysis. So let's skip
        if (Strings.isNullOrEmpty(article.getTitle()) || article.getTitle().length() < 5) {
            return null;
        }

        parseContentsFromLink(rssItem, article);

        return article;
    }

    private void parseTitleAuthor(String title, Article article) {
        article.setTitle(title);
    }

    private void parseContentsFromLink(SyndEntry rssItem, Article article) {
        String rssLink = rssItem.getLink();

        Document doc;
        try {
            doc = Jsoup.connect(rssLink).timeout(6000).get();
        } catch (IOException e) {
            //LOG.error(rssLink + "\n", e);
            return;
        }


        Elements contentsArea;

        if (rssLink.contains("interview365")) {
            contentsArea = doc.select("#IDContents");
        } else if (rssLink.contains("khan")) {
            contentsArea = doc.select("#_article");
        } else if (rssLink.contains("segye")) {
            contentsArea = doc.select("#article_txt");
        } else if (rssLink.contains("asiae")) {
            contentsArea = doc.select(".article");
        } else if (rssLink.contains("chosun")) {
            contentsArea = doc.select(".par");
        } else if (rssLink.contains("dailian")) {
            contentsArea = doc.select(".par");
        } else if (rssLink.contains("newsen")) {
            contentsArea = doc.select("#CLtag");
        } else if (rssLink.contains("sportsseoul")) {
            contentsArea = doc.select("#content_area");
        } else if (rssLink.contains("fnnews")) {
            contentsArea = doc.select("#contTxt");
        } else if (rssLink.contains("kookje")) {
            contentsArea = doc.select("#news_textArea");
        } else if (rssLink.contains("hankooki")) {
            contentsArea = doc.select("#GS_Content");
        } else if (rssLink.contains("ittoday")
                || rssLink.contains("unionpress")
                || rssLink.contains("yonhapnews")
                || rssLink.contains("itimes")
                || rssLink.contains("newsis")) {

            contentsArea = doc.select("#articleBody");
        } else if (rssLink.contains("hankyung")
                || rssLink.contains("ahatv")) {

            contentsArea = doc.select("#sstvarticle");
        } else if (rssLink.contains("sportsworldi")) {
            contentsArea = doc.select("#article_content");
        } else if (rssLink.contains("tvdaily")
                || rssLink.contains("etoday")
                || rssLink.contains("ytn")
                || rssLink.contains("vop")) {

            contentsArea = doc.select("#CmAdContent");
        } else {
//			LOG.error("Fail to parsing => Link:{}, ContentId:{}", rssLink, contentId);
            return;
        }

        article.setContents(ContentsAnalysisService.removeInvalidWordsForKR(contentsArea.text().trim()));


        // extract the link of a image
        String imgLink = contentsArea.select("img").attr("src");
        if (imgLink.contains("gif")) {
            return;
        }
        article.setImg(imgLink);
    }
}
