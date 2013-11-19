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

import com.infoc.crawler.CrawlScheduler;
import com.infoc.crawler.DaumNewsCrawler;
import com.infoc.crawler.GoogleNewsCrawler;
import com.infoc.crawler.NaverNewsCrawler;
import com.infoc.domain.Article;
import com.infoc.service.CollectionService;
import com.infoc.service.ContentsAnalysisService;

@Controller
public class HomeController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = {"/", "/home"})
	public String home(Model model) throws Exception {

		model.addAttribute("articleMap", CollectionService.CACHE);
		model.addAttribute("currentHour", DateTime.now(DateTimeZone.forID("Asia/Seoul")).getHourOfDay());
		model.addAttribute("currentDay", DateTime.now(DateTimeZone.forID("Asia/Seoul")).toDate());
		
		model.addAttribute("econ", CollectionService.ECON_INFO);
		
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
		
		GoogleNewsCrawler g = new GoogleNewsCrawler();
		list.addAll(g.createArticlList());

		for (Article article : list) {
			// create the main contents
			ContentsAnalysisService.createMainSentence(article);

			// add to the store
			CollectionService.add(article);
		}

		return "success";
	}
	
	@RequestMapping(value = "/run")
	@ResponseBody
	public String runScheduler() {
		LOG.info("{}", DateTime.now(DateTimeZone.forID("Asia/Seoul")));
		
		CrawlScheduler.runShcedule();
		
		return "success";
	}
}
