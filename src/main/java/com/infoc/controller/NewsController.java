package com.infoc.controller;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infoc.service.CollectionService;

@Controller
public class NewsController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(NewsController.class);

	private void getCommonInfo(Model model) {
		model.addAttribute("econ", CollectionService.ECON_INFO);
		model.addAttribute("currentHour", DateTime.now(DateTimeZone.forID("Asia/Seoul")).getHourOfDay());
		model.addAttribute("currentDay", DateTime.now(DateTimeZone.forID("Asia/Seoul")).toDate());
	}

	@RequestMapping(value = {"/", "/main"})
	public String getMain(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.TODAY_CACHE);
		model.addAttribute("menu","main");
		return "/main";
	}

	@RequestMapping(value = "/politics")
	public String getPolitics(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.POLITICS_CACHE);
		model.addAttribute("menu","politics");
		return "/main";
	}

	@RequestMapping(value = "/econ")
	public String getEcon(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.ECON_CACHE);
		model.addAttribute("menu","econ");
		return "/main";
	}

	@RequestMapping(value = "/society")
	public String getSociety(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.SOCIETY_CACHE);
		model.addAttribute("menu","society");
		return "/main";
	}

	@RequestMapping(value = "/culture")
	public String getCulture(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.CULTURE_CACHE);
		model.addAttribute("menu","culture");
		return "/main";
	}

	@RequestMapping(value = "/ent")
	public String getEnt(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.ENT_CACHE);
		model.addAttribute("menu","ent");
		return "/main";
	}

	@RequestMapping(value = "/sport")
	public String getSport(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.SPORT_CACHE);
		model.addAttribute("menu","sport");
		return "/main";
	}

	@RequestMapping(value = "/it")
	public String getIt(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.IT_CACHE);
		model.addAttribute("menu","it");
		return "/main";
	}
}
