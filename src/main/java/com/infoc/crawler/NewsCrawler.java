/*
 * @(#)NewsCrawler.java $version 2013. 10. 25.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.crawler;

import java.util.List;

import com.infoc.domain.Article;

/**
 * @author NBP
 */
public interface NewsCrawler {
	public List<Article> createArticlList();
}
