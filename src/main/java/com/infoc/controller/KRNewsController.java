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
	
	public String getArticlesByDate(Model model, Map<Integer, List<Article>> cacheMap, final String date,
			ArticleSection section, String menuName, int page) throws Exception {

		DateTime currTime = DateTime.now(DateTimeZone.forID("Asia/Seoul"));
		DateTime reqTime = new DateTime(Long.parseLong(date), DateTimeZone.forID("Asia/Seoul"));
		LOG.debug("reqTime: {}, page: {}", reqTime.toDate(), page);
		
		Map<Integer, List<Article>> articleListMap = new HashMap<Integer, List<Article>>();
		
		if (reqTime.getDayOfMonth() == currTime.getDayOfMonth()) {
			articleListMap = CollectionService.getArticlesByCurrentTime(cacheMap, page);
			model.addAttribute("end", false);
		} else {
			// 전날의 "주요 뉴스" 가져오기
			Map<Integer, List<Article>> oldArticleListMap = 
					articleService.getArticlesByPubDateAndSection(reqTime.toDate(), section);
			
			if(oldArticleListMap == null || oldArticleListMap.isEmpty()) {
				model.addAttribute("end", true);
			} else {
				articleListMap = CollectionService.getArticlesByPage(oldArticleListMap, page);
				if (articleListMap.isEmpty() && page == 0) {
					model.addAttribute("end", true);
				} else {
					model.addAttribute("end", false);	
				}
			}
		}
			
		model.addAttribute("articleMap", articleListMap);
		model.addAttribute("menu", menuName);
		model.addAttribute("requestDay", reqTime.toDate());
		
		return "/common/articles";
	}
	
	private void getCommonInfo(Model model) {
		model.addAttribute("econ", CollectionService.ECON_INFO);
		model.addAttribute("currentDay", DateTime.now(DateTimeZone.forID("Asia/Seoul")).toDate());
	}

	@RequestMapping(value = {"/", "/main"})
	public String getMain(Model model) throws Exception {
		
		getCommonInfo(model);
		model.addAttribute("requestDay", DateTime.now(DateTimeZone.forID("Asia/Seoul")).toDate());
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.TODAY_CACHE));
		model.addAttribute("menu", "main");
		return "/main";
	}

	@RequestMapping(value = "/main/date/{date}/page/{page}")
	public String getMainByDate(Model model, 
			@PathVariable("date") final String date, 
			@PathVariable("page") int page) throws Exception {

		return getArticlesByDate(model, CollectionService.TODAY_CACHE, date, ArticleSection.TODAY, "main", page);
	}
	
	@RequestMapping(value = "/politics")
	public String getPolitics(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.POLITICS_CACHE));
		model.addAttribute("menu", "politics");
		return "/main";
	}

	@RequestMapping(value = "/politics/date/{date}/page/{page}")
	public String getPoliticsByDate(Model model, 
			@PathVariable("date") final String date, 
			@PathVariable("page") int page) throws Exception {
		
		return getArticlesByDate(model, CollectionService.POLITICS_CACHE, date, ArticleSection.POLITICS, "politics", page);
	}	
	
	@RequestMapping(value = "/econ")
	public String getEcon(Model model) throws Exception {
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.ECON_CACHE));
		model.addAttribute("menu", "econ");
		return "/main";
	}
	
	@RequestMapping(value = "/econ/date/{date}/page/{page}")
	public String getEconByDate(Model model, 
			@PathVariable("date") final String date, 
			@PathVariable("page") int page) throws Exception {
		
		return getArticlesByDate(model, CollectionService.ECON_CACHE, date, ArticleSection.ECON, "econ", page);
	}
	
	@RequestMapping(value = "/society")
	public String getSociety(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.SOCIETY_CACHE));
		model.addAttribute("menu", "society");
		return "/main";
	}
	@RequestMapping(value = "/society/date/{date}/page/{page}")
	public String getSocietyByDate(Model model, 
			@PathVariable("date") final String date, 
			@PathVariable("page") int page) throws Exception {
		
		return getArticlesByDate(model, CollectionService.SOCIETY_CACHE, date, ArticleSection.SOCIETY, "society", page);
	}

	@RequestMapping(value = "/culture")
	public String getCulture(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.CULTURE_CACHE));
		model.addAttribute("menu", "culture");
		return "/main";
	}
	@RequestMapping(value = "/culture/date/{date}/page/{page}")
	public String getCultureByDate(Model model, 
			@PathVariable("date") final String date, 
			@PathVariable("page") int page) throws Exception {
		
		return getArticlesByDate(model, CollectionService.CULTURE_CACHE, date, ArticleSection.CULTURE, "culture", page);
	}

	@RequestMapping(value = "/ent")
	public String getEnt(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.ENT_CACHE));
		model.addAttribute("menu", "ent");
		return "/main";
	}
	@RequestMapping(value = "/ent/date/{date}/page/{page}")
	public String getEntByDate(Model model, 
			@PathVariable("date") final String date, 
			@PathVariable("page") int page) throws Exception {
		
		return getArticlesByDate(model, CollectionService.ENT_CACHE, date, ArticleSection.ENT, "ent", page);
	}

	@RequestMapping(value = "/sport")
	public String getSport(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.SPORT_CACHE));
		model.addAttribute("menu", "sport");
		return "/main";
	}
	@RequestMapping(value = "/sport/date/{date}/page/{page}")
	public String getSportByDate(Model model, 
			@PathVariable("date") final String date, 
			@PathVariable("page") int page) throws Exception {
		
		return getArticlesByDate(model, CollectionService.SPORT_CACHE, date, ArticleSection.SPORT, "sport", page);
	}
	
	@RequestMapping(value = "/it")
	public String getIt(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.IT_CACHE));
		model.addAttribute("menu", "it");
		return "/main";
	}
	@RequestMapping(value = "/it/date/{date}/page/{page}")
	public String getITByDate(Model model, 
			@PathVariable("date") final String date, 
			@PathVariable("page") int page) throws Exception {
		
		return getArticlesByDate(model, CollectionService.IT_CACHE, date, ArticleSection.IT, "it", page);
	}
	
	@RequestMapping(value = "/others")
	public String getUserNews(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", CollectionService.getArticlesByCurrentTime(CollectionService.OTHERS_CACHE));
		model.addAttribute("menu", "others");
		return "/main";
	}
	@RequestMapping(value = "/others/date/{date}/page/{page}")
	public String geOthersByDate(Model model, 
			@PathVariable("date") final String date, 
			@PathVariable("page") int page) throws Exception {
		
		return getArticlesByDate(model, CollectionService.OTHERS_CACHE, date, ArticleSection.OTHERS, "others", page);
	}
}
