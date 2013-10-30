package com.infoc.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infoc.crawler.DaumNewsCrawler;
import com.infoc.crawler.NaverNewsCrawler;
import com.infoc.domain.Article;
import com.infoc.rss.Nnews;
import com.infoc.service.CollectionService;
import com.infoc.service.ContentsAnalysisService;

@Controller
public class HomeController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = {"/", "/home"})
	public String home(Model model) {

		// TODO: need to run by timer separately ///////////////////////////////
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
		/////////////////////////////////////////////////////////////
		
		
		model.addAttribute("articleMap", CollectionService.CACHE);
		model.addAttribute("currentHour", DateTime.now(DateTimeZone.forID("Asia/Seoul")).getHourOfDay());

		return "/home";
	}

}
