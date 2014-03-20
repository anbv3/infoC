package com.infoc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.infoc.domain.Article;
import com.infoc.repository.ArticleRepository;
import com.infoc.service.CollectionService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public class UserControllerTest {
	private static final Logger LOG = LoggerFactory.getLogger(UserControllerTest.class);
	
	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private CollectionService collectionService;
	
	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	/**
	 * /users/{name} 을 호출하고
	 * 결과에 user 속성이 추가되어 있는지 확인 및
	 * 정상적으로 view 페이지로 이동하는지 확인
	 * 
	 * @throws Exception
	 */
	@Test
	public void getUsers() throws Exception {
		this.mockMvc.perform(get("/users/form")).andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attributeHasNoErrors("user"))
			.andExpect(forwardedUrl("users/form"));
	}

	@Test
	public void testArticleBeanInit() throws Exception {
		try {
			Article a = new Article();
			a.setPubDate(DateTime.now(DateTimeZone.forID("Asia/Seoul")).toDate());
			a.setTitle("xxxxxxxx");
			
			collectionService.add(a);
			//articleRepository.save(a);
			
			LOG.debug("{}", articleRepository.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
