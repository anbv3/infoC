package com.infoc.controller;

import com.infoc.service.CollectionService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infoc.rss.Dnews;
import com.infoc.rss.Gnews;
import com.infoc.rss.Nnews;

@Controller
public class HomeController extends BaseController {

	@RequestMapping(value = { "/", "/home" })
	public String home(Model model) {

		new Nnews().getNews();
		new Gnews().getNews();
		new Dnews().getNews();

		model.addAttribute("articleMap", CollectionService.CACHE);
		model.addAttribute("currentHour", new DateTime().getHourOfDay());

		return "/home";
	}

}
