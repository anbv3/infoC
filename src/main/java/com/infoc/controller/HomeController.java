package com.infoc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infoc.domain.Collector;
import com.infoc.rss.Gnews;
import com.infoc.rss.Nnews;

@Controller
public class HomeController extends BaseController {

	@RequestMapping(value = { "/", "/home" })
	public String home(Model model) {

//		List<Article> articleList = (new Gnews()).getNews();
//		articleList.addAll((new Nnews()).getNews());
		new Gnews().getNews();
		new Nnews().getNews();
		
		model.addAttribute("articleMap", Collector.CACHE);
		
		return "/home";
	}

}
