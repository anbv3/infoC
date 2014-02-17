package com.infoc.controller;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infoc.service.USCollectionService;

@Controller
@RequestMapping(value = "/us")
public class USNewsController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(USNewsController.class);

	private void getCommonInfo(Model model) {
		//model.addAttribute("econ", USCollectionService.ECON_INFO);
		model.addAttribute("currentHour", DateTime.now(DateTimeZone.forID("Asia/Seoul")).getHourOfDay());
		model.addAttribute("currentDay", DateTime.now(DateTimeZone.forID("Asia/Seoul")).toDate());
	}

	@RequestMapping(value = "/main")
	public String getMain(Model model) throws Exception {
		getCommonInfo(model);
		
		model.addAttribute("articleMap", USCollectionService.TODAY_CACHE);
		model.addAttribute("menu","main");
		return "/us-main";
	}

	@RequestMapping(value = "/politics")
	public String getPolitics(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.POLITICS_CACHE);
		model.addAttribute("menu","politics");
		return "/us-main";
	}

	@RequestMapping(value = "/econ")
	public String getEcon(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.ECON_CACHE);
		model.addAttribute("menu","econ");
		return "/us-main";
	}

	@RequestMapping(value = "/society")
	public String getSociety(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.SOCIETY_CACHE);
		model.addAttribute("menu","society");
		return "/us-main";
	}

	@RequestMapping(value = "/culture")
	public String getCulture(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.CULTURE_CACHE);
		model.addAttribute("menu","culture");
		return "/us-main";
	}

	@RequestMapping(value = "/ent")
	public String getEnt(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.ENT_CACHE);
		model.addAttribute("menu","ent");
		return "/us-main";
	}

	@RequestMapping(value = "/sport")
	public String getSport(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.SPORT_CACHE);
		model.addAttribute("menu","sport");
		return "/us-main";
	}

	@RequestMapping(value = "/it")
	public String getIt(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.IT_CACHE);
		model.addAttribute("menu","it");
		return "/us-main";
	}
}
