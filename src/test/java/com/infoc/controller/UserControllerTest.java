/*
 * @(#)UserControllerTest.java $version 2013. 5. 1.
 *
 */

package com.infoc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public class UserControllerTest {

	@Autowired
	private WebApplicationContext wac;

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

}
