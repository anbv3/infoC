package com.infoc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infoc.util.RSSReader;
import com.sun.syndication.feed.synd.SyndEntry;


@Controller
public class HomeController extends BaseController {
	
	
	private static String G_NEWS = "https://news.google.co.kr/nwshp?hl=ko&output=rss";
	private static String N_NEWS = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=2";
	
	@RequestMapping(value={"/", "/home"})
	public String home(Model model) {
		
		String testURL = "http://news.search.naver.com/newscluster/rss.nhn?type=0&rss_idx=3";
		List<SyndEntry> articleList = RSSReader.getArticleList(testURL);
		articleList.remove(articleList.size()-1);
		model.addAttribute("articleList", articleList);
		
		
		String testURL2 = "http://news.google.co.kr/news?pz=1&cf=all&ned=kr&hl=ko&topic=p&output=rss";
		List<SyndEntry> articleList2 = RSSReader.getArticleList(testURL2);
		articleList2.remove(articleList2.size()-1);
		model.addAttribute("articleList2", articleList2);
		
		
		return "/home";
	}
	
}
