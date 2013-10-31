package com.infoc.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.infoc.crawler.DaumNewsCrawler;
import com.infoc.crawler.NaverNewsCrawler;
import com.infoc.domain.Article;
import com.infoc.service.CollectionService;
import com.infoc.service.ContentsAnalysisService;

@Controller
public class HomeController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = {"/", "/home"})
	public String home(Model model) {

		model.addAttribute("articleMap", CollectionService.CACHE);
		model.addAttribute("currentHour", DateTime.now(DateTimeZone.forID("Asia/Seoul")).getHourOfDay());

		return "/home";
	}

	@RequestMapping(value = "/crawl")
	@ResponseBody
	public String crawl() {
		LOG.info("{}", DateTime.now(DateTimeZone.forID("Asia/Seoul")));
		
		DaumNewsCrawler d = new DaumNewsCrawler();
		List<Article> list = d.createArticlList();

		NaverNewsCrawler n = new NaverNewsCrawler();
		list.addAll(n.createArticlList());

		for (Article article : list) {
			// create the main contents
			ContentsAnalysisService.createMainSentence(article);

			// add to the store
			CollectionService.add(article);
		}

		return "success";
	}
}
