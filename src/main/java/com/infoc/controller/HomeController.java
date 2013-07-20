package com.infoc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infoc.domain.Article;
import com.infoc.rss.Gnews;
import com.infoc.rss.Nnews;

@Controller
public class HomeController extends BaseController {

	@RequestMapping(value = { "/", "/home" })
	public String home(Model model) {

//		List<Article> articleList = (new Gnews()).getNews();
//		articleList.addAll((new Nnews()).getNews());
		model.addAttribute("articleList", (new Gnews()).getNews());
		model.addAttribute("articleList2", (new Nnews()).getNews());
		return "/home";
	}

}
