/*
 * @(#)DaumNewsCrawler.java $version 2013. 10. 25.
 */

package com.infoc.crawler;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.infoc.domain.Article;
import com.infoc.enumeration.ArticleSection;
import com.infoc.service.CollectionService;
import com.infoc.service.ContentsAnalysisService;
import com.infoc.util.RSSCrawler;
import com.sun.syndication.feed.synd.SyndEntry;

public class UserNewsCrawler implements NewsCrawler {
	private static final Logger LOG = LoggerFactory.getLogger(UserNewsCrawler.class);

	private static String LIKELINK = "http://feeds.feedburner.com/likelink-recent";
	private static String NEWSTAPA = "http://www.newstapa.com/feed";
	private static String NEWSPEPPER = "http://newspeppermint.com/feed";
	private static String CLIEN_NEWS = "http://feeds.feedburner.com/Clien--news";
	private static String SLOW_NEWS = "http://feeds.feedburner.com/slownews";

	private List<Article> articleList = new ArrayList<>();

	@Override
	public List<Article> createArticlList() {
		LOG.debug("get RSS from USER.");

		createListBySection(LIKELINK, ArticleSection.USER);
		createListBySection(NEWSTAPA, ArticleSection.USER);
		createListBySection(NEWSPEPPER, ArticleSection.USER);
		createListBySection(CLIEN_NEWS, ArticleSection.USER);
		createListBySection(SLOW_NEWS, ArticleSection.USER);

		return this.articleList;
	}

	private void createListBySection(String rssUrl, ArticleSection section) {
		for (SyndEntry item : RSSCrawler.getArticleList(rssUrl)) {
			Article article = parseRSSItem(item, section);
			
			if (article == null) {
				continue;
			}
			
			if (article.getContents().length() < 10) {
				continue;
			}
			
			if (article.getPubDate().isBefore(DateTime.now(DateTimeZone.forID("Asia/Seoul")).minusDays(1))) {
				return;
			}
			
			// create the main contents
			ContentsAnalysisService.createMainSentence(article);
			
			// add to the store
			CollectionService.add(article);
		}
	}

	private Article parseRSSItem(SyndEntry rssItem, ArticleSection section) {
		Article article = new Article();
		article.setSection(section);
		article.setAuthor(rssItem.getAuthor());
		article.setLink(rssItem.getLink());
		article.setPubDate(new DateTime(rssItem.getPublishedDate(), DateTimeZone.forID("Asia/Seoul")));
		article.setTitle(ContentsAnalysisService.clearInvalidWords(rssItem.getTitle()));
		if (Strings.isNullOrEmpty(article.getTitle()) || article.getTitle().length() < 5) {
			return null;
		}

		article.createContentsFromLink();
		if (Strings.isNullOrEmpty(article.getContents())) {
			article.setContents(rssItem.getDescription().getValue());
		}

		article.setContents(ContentsAnalysisService.clearInvalidWords(article.getContents()));

		return article;
	}

}
