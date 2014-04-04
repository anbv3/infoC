package com.infoc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infoc.domain.Article;
import com.infoc.enumeration.ArticleSection;
import com.infoc.service.ArticleService;
import com.infoc.service.CollectionService;

@Controller
@RequestMapping(value = {"/", "/kr"})
public class KRNewsController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(KRNewsController.class);
	
	@Autowired
	ArticleService articleService;
	
	private void getCommonInfo(Model model) {
		model.addAttribute("econ", CollectionService.ECON_INFO);
		model.addAttribute("currentDay", DateTime.now(DateTimeZone.forID("Asia/Seoul")).toDate());
	}

	@RequestMapping(value = {"/", "/main"})
	public String getMain(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.TODAY_CACHE));
		model.addAttribute("menu", "main");
		return "/main";
	}

	@RequestMapping(value = "/main/date/{date}/page/{page}")
	public String getMainByDate(Model model, 
			@PathVariable("date") final String date, 
			@PathVariable("page") int page) throws Exception {

		DateTime currTime = DateTime.now(DateTimeZone.forID("Asia/Seoul"));
		DateTime reqTime = new DateTime(Long.parseLong(date));
		LOG.debug("reqTime: {}, page: {}", reqTime.toDate(), page);
		
		Map<Integer, List<Article>> articleListMap = new HashMap<Integer, List<Article>>();
		
		if (reqTime.getDayOfMonth() == currTime.getDayOfMonth()) {
			articleListMap = CollectionService.getArticlesByCurrentTime(CollectionService.TODAY_CACHE, page);
			model.addAttribute("end", false);
		} else {
			// 전날의 "주요 뉴스" 가져오기
			Map<Integer, List<Article>> oldArticleListMap = articleService.getArticlesByPubDateAndSection(reqTime.toDate(), ArticleSection.TODAY);
			
			if(oldArticleListMap == null || oldArticleListMap.isEmpty()) {
				model.addAttribute("end", true);
			} else {
				articleListMap = CollectionService.getArticlesByCurrentTime(oldArticleListMap, page);
				if (articleListMap.isEmpty() && page == 0) {
					model.addAttribute("end", true);
				} else {
					model.addAttribute("end", false);	
				}
			}
		}
		
			
		model.addAttribute("articleMap", articleListMap);
		model.addAttribute("menu", "main");
		model.addAttribute("currentDay", reqTime.toDate());
		
		return "/common/articles";
	}
	
	@RequestMapping(value = "/politics")
	public String getPolitics(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.POLITICS_CACHE));
		model.addAttribute("menu", "politics");
		return "/main";
	}

	@RequestMapping(value = "/politics/{page}")
	public String getPolitics(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.POLITICS_CACHE, page));
		model.addAttribute("menu", "politics");
		return "/common/articles";
	}
	
	
	@RequestMapping(value = "/econ")
	public String getEcon(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.ECON_CACHE));
		model.addAttribute("menu", "econ");
		return "/main";
	}
	
	@RequestMapping(value = "/econ/{page}")
	public String getEcon(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.ECON_CACHE, page));
		model.addAttribute("menu", "econ");
		return "/common/articles";
	}

	@RequestMapping(value = "/society")
	public String getSociety(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.SOCIETY_CACHE));
		model.addAttribute("menu", "society");
		return "/main";
	}
	@RequestMapping(value = "/society/{page}")
	public String getSociety(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.SOCIETY_CACHE, page));
		model.addAttribute("menu", "society");
		return "/common/articles";
	}

	@RequestMapping(value = "/culture")
	public String getCulture(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.CULTURE_CACHE));
		model.addAttribute("menu", "culture");
		return "/main";
	}
	@RequestMapping(value = "/culture/{page}")
	public String getCulture(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.CULTURE_CACHE, page));
		model.addAttribute("menu", "culture");
		return "/common/articles";
	}

	@RequestMapping(value = "/ent")
	public String getEnt(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.ENT_CACHE));
		model.addAttribute("menu", "ent");
		return "/main";
	}
	@RequestMapping(value = "/ent/{page}")
	public String getEnt(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.ENT_CACHE, page));
		model.addAttribute("menu", "ent");
		return "/common/articles";
	}

	@RequestMapping(value = "/sport")
	public String getSport(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.SPORT_CACHE));
		model.addAttribute("menu", "sport");
		return "/main";
	}
	@RequestMapping(value = "/sport/{page}")
	public String getSport(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.SPORT_CACHE, page));
		model.addAttribute("menu", "sport");
		return "/common/articles";
	}
	
	@RequestMapping(value = "/it")
	public String getIt(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.IT_CACHE));
		model.addAttribute("menu", "it");
		return "/main";
	}
	@RequestMapping(value = "/it/{page}")
	public String getIt(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.IT_CACHE, page));
		model.addAttribute("menu", "it");
		return "/common/articles";
	}
	
	@RequestMapping(value = "/others")
	public String getUserNews(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.OTHERS_CACHE));
		model.addAttribute("menu", "others");
		return "/main";
	}
	@RequestMapping(value = "/others/{page}")
	public String getUserNews(Model model, @PathVariable("page") final int page) throws Exception {
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.OTHERS_CACHE, page));
		model.addAttribute("menu", "others");
		return "/common/articles";
	}
}
