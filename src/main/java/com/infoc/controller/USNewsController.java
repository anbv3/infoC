package com.infoc.controller;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infoc.service.USCollectionService;

@Controller
@RequestMapping(value = "/us")
public class USNewsController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(USNewsController.class);

	private void getCommonInfo(Model model) {
		model.addAttribute("currentDay", DateTime.now(DateTimeZone.forID("Asia/Seoul")).toDate());
	}

	@RequestMapping(value = "/main")
	public String getMain(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.TODAY_CACHE));
		model.addAttribute("menu","main");
		return "/us-main";
	}
	
	@RequestMapping(value = "/main/{page}")
	public String getMain(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.TODAY_CACHE, page));
		model.addAttribute("menu","main");
		return "/common/articles";
	}
	
	@RequestMapping(value = "/politics")
	public String getPolitics(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.POLITICS_CACHE));
		model.addAttribute("menu","politics");
		return "/us-main";
	}
	@RequestMapping(value = "/politics/{page}")
	public String getPolitics(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.POLITICS_CACHE, page));
		model.addAttribute("menu","politics");
		return "/common/articles";
	}
	

	@RequestMapping(value = "/econ")
	public String getEcon(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.ECON_CACHE));
		model.addAttribute("menu","econ");
		return "/us-main";
	}
	@RequestMapping(value = "/econ/{page}")
	public String getEcon(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.ECON_CACHE, page));
		model.addAttribute("menu","econ");
		return "/common/articles";
	}

	@RequestMapping(value = "/society")
	public String getSociety(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.SOCIETY_CACHE));
		model.addAttribute("menu","society");
		return "/us-main";
	}
	@RequestMapping(value = "/society/{page}")
	public String getSociety(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.SOCIETY_CACHE, page));
		model.addAttribute("menu","society");
		return "/common/articles";
	}
	
	@RequestMapping(value = "/culture")
	public String getCulture(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.CULTURE_CACHE));
		model.addAttribute("menu","culture");
		return "/us-main";
	}
	@RequestMapping(value = "/culture/{page}")
	public String getCulture(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.CULTURE_CACHE, page));
		model.addAttribute("menu","culture");
		return "/common/articles";
	}
	
	@RequestMapping(value = "/ent")
	public String getEnt(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.ENT_CACHE));
		model.addAttribute("menu","ent");
		return "/us-main";
	}
	@RequestMapping(value = "/ent/{page}")
	public String getEnt(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.ENT_CACHE, page));
		model.addAttribute("menu","ent");
		return "/common/articles";
	}

	@RequestMapping(value = "/sport")
	public String getSport(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.SPORT_CACHE));
		model.addAttribute("menu","sport");
		return "/us-main";
	}
	@RequestMapping(value = "/sport/{page}")
	public String getSport(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.SPORT_CACHE, page));
		model.addAttribute("menu","sport");
		return "/common/articles";
	}

	@RequestMapping(value = "/it")
	public String getIt(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.IT_CACHE));
		model.addAttribute("menu","it");
		return "/us-main";
	}
	@RequestMapping(value = "/it/{page}")
	public String getIt(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.IT_CACHE, page));
		model.addAttribute("menu","it");
		return "/common/articles";
	}
}
