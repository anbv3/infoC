package com.infoc.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.infoc.enumeration.ArticleSection;


/**
 * @author NBP
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public class ArticleServiceTest {
	private static final Logger LOG = LoggerFactory.getLogger(ArticleServiceTest.class);
	
	@Autowired
	private ArticleService articleService;
	
	@Test
	public void getArticlesByMainContents() throws Exception {
		
		LOG.debug("{}", articleService.getArticlesByMainContents("KR", ArticleSection.TODAY, "%기%", new PageRequest(0, 5)));
	}
	
}
