/*
 * @(#)NewsCrawler.java $version 2013. 10. 25.
 */

package com.infoc.crawler;

import java.util.List;

import com.infoc.domain.Article;

public interface NewsCrawler {
	public List<Article> createArticleList();
}
