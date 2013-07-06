package com.infoc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infoc.util.RSSReader;
import com.sun.syndication.feed.synd.SyndEntry;


@Controller
public class HomeController extends BaseController {
	
	@RequestMapping(value={"/", "/home"})
	public String home(Model model) {
		
		String testURL = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=3";
		List<SyndEntry> articleList = RSSReader.getArticleList(testURL);
		
		
		model.addAttribute("articleList", articleList);
		return "/home";
	}
	
}
