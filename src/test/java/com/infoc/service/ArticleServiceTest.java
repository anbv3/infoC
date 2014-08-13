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
	public void testGetArticlesByMainContents() throws Exception {
		
		LOG.debug("{}", articleService.getArticlesByMainContents("KR", ArticleSection.TODAY, "기", new PageRequest(0, 5)));
	}


    @Test
    public void testGetArticleByTitleAndLink() throws Exception {
        String t = "He was on the disabled list from April 18 to June 5, came back for a week as a reliever and then went back on the DL with more elbow problems. Rios expected to return to lineup Wednesday play video TB@TEX: Rios discusses right ankle injury Facebook Twitter Email ARLINGTON -- Outfielder Alex Rios was out of the lineup for a second straight game on Tuesday with a sprained right ankle, but there is a good possibility of being back in the lineup on Wednesday.";
        String l = "http://mlb.mlb.com/news/article.jsp?ymd=20140812&content_id=89250676&notebook_id=89268496&vkey=notebook_tex&c_id=tex";

        LOG.debug("{}", articleService.getArticleByTitleAndLink(t, l));
        LOG.debug("{}", articleService.getArticleByLink(l));
    }

}
