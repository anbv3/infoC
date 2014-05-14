/*
 * @(#)CommonController.java $version 2014. 5. 14.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.infoc.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author NBP
 */
@Controller
@RequestMapping
public class CommonController {
	private static final Logger LOG = LoggerFactory.getLogger(CommonController.class);
	
	@RequestMapping(value = {"/", "/main"})
	public String redirectByLocale(Model model, Locale locale) throws Exception {
		LOG.debug("{}", locale);
		
		if (locale.getLanguage().equals(new Locale("ko").getLanguage()) || 
			locale.getLanguage().equals(new Locale("ko_KR").getLanguage())) {
			
			return "redirect:/kr/main";
		} else {
			return "redirect:/us/main";
		}
	}
}
