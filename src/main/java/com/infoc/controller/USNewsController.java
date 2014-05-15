package com.infoc.controller;

import com.google.common.base.Strings;
import com.infoc.domain.Article;
import com.infoc.enumeration.ArticleSection;
import com.infoc.service.ArticleService;
import com.infoc.service.USCollectionService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/us")
public class USNewsController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(USNewsController.class);

	@Autowired
	ArticleService articleService;

	public String getArticlesByDate(Model model, Map<Integer, List<Article>> cacheMap, final String date,
		ArticleSection section, String menuName, int page, String search) throws Exception {

		DateTime currTime = new DateTime(DateTimeZone.forID("Asia/Seoul"));
		DateTime reqTime = new DateTime(Long.parseLong(date), DateTimeZone.forID("Asia/Seoul"));

		Map<Integer, List<Article>> articleListMap = new HashMap<Integer, List<Article>>();

		if (reqTime.getDayOfMonth() == currTime.getDayOfMonth()) {
			articleListMap = USCollectionService.getArticlesByCurrentTime(cacheMap, page, search);
			model.addAttribute("end", false);
		} else {
			model.addAttribute("end", true);
			
			// 전날 "뉴스" 가져오기
			Map<Integer, List<Article>> oldArticleListMap =
				articleService.getArticlesByPubDateAndSection(reqTime.toDate(), "US", section);

			if (oldArticleListMap == null || oldArticleListMap.isEmpty()) {
				model.addAttribute("end", true);
			} else {
				articleListMap = USCollectionService.getArticlesByPage(oldArticleListMap, page, search);
				if (articleListMap.isEmpty() && page == 0) {
					model.addAttribute("end", true);
				} else {
					model.addAttribute("end", false);
				}
			}
		}

		model.addAttribute("articleMap", articleListMap);
		model.addAttribute("menu", menuName);
		model.addAttribute("requestDay", reqTime.toString(DateTimeFormat.forPattern("yyyy-dd-MM")));

		return "/common/articles";
	}

	private void getCommonInfo(Model model) {
		DateTime dTime = new DateTime(DateTimeZone.forID("Asia/Seoul"));
		model.addAttribute("initDay", dTime.toString(DateTimeFormat.forPattern("MM/dd/yyyy hh:mm:ss")));
		model.addAttribute("currentDay", dTime.toString(DateTimeFormat.forPattern("yyyy.MM.dd")));
		model.addAttribute("requestDay", dTime.toString(DateTimeFormat.forPattern("yyyy-dd-MM")));
	}
	
	@RequestMapping(value = "/search/{section}")
	public String getMainBySearch(Pageable pageable, Model model,
			@PathVariable("section") final String section,
			@RequestParam(value = "q") String query) throws Exception {
		LOG.debug("section:{}, query: {}, {}", section, query, ToStringBuilder.reflectionToString(pageable));
		
		Page<Article> articleList = articleService.getArticlesByMainContents("US", ArticleSection.find(section), query, pageable);
		model.addAttribute("page", articleList);
		model.addAttribute("pubDate", articleService.extractStartAndEndDate(articleList));
		model.addAttribute("menu", section);
		
		return "/common/searched-articles";
	}
	
	@RequestMapping(value = "/main")
	public String getMain(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.TODAY_CACHE));
		model.addAttribute("menu","main");
		return "/us-main";
	}
	
	@RequestMapping(value = "/main/date/{date}/page/{page}")
	public String getMainByDate(Model model,
		@PathVariable("date") final String date,
		@PathVariable("page") int page,
		@RequestParam(value = "search", required = false) String search) throws Exception {

		String decodedSearchInput = null;
		if (!Strings.isNullOrEmpty(search)) {
			decodedSearchInput = URLDecoder.decode(search, "UTF-8");
		}

		return getArticlesByDate(model, USCollectionService.TODAY_CACHE, date, ArticleSection.TODAY, "main", page, decodedSearchInput);
	}
	
	@RequestMapping(value = "/politics")
	public String getPolitics(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.POLITICS_CACHE));
		model.addAttribute("menu","politics");
		return "/us-main";
	}
	
	@RequestMapping(value = "/politics/date/{date}/page/{page}")
	public String getPoliticsByDate(Model model,
		@PathVariable("date") final String date,
		@PathVariable("page") int page,
		@RequestParam(value = "search", required = false) String search) throws Exception {

		String decodedSearchInput = null;
		if (!Strings.isNullOrEmpty(search)) {
			decodedSearchInput = URLDecoder.decode(search, "UTF-8");
		}

		return getArticlesByDate(model, USCollectionService.POLITICS_CACHE, date, ArticleSection.POLITICS, "politics", page, decodedSearchInput);
	}
	

	@RequestMapping(value = "/econ")
	public String getEcon(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.ECON_CACHE));
		model.addAttribute("menu","econ");
		return "/us-main";
	}
	
	@RequestMapping(value = "/econ/date/{date}/page/{page}")
	public String getEconByDate(Model model,
		@PathVariable("date") final String date,
		@PathVariable("page") int page,
		@RequestParam(value = "search", required = false) String search) throws Exception {

		String decodedSearchInput = null;
		if (!Strings.isNullOrEmpty(search)) {
			decodedSearchInput = URLDecoder.decode(search, "UTF-8");
		}

		return getArticlesByDate(model, USCollectionService.ECON_CACHE, date, ArticleSection.ECON, "econ", page, decodedSearchInput);
	}
	
	@RequestMapping(value = "/society")
	public String getSociety(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.SOCIETY_CACHE));
		model.addAttribute("menu","society");
		return "/us-main";
	}
	
	@RequestMapping(value = "/society/date/{date}/page/{page}")
	public String getSocietyByDate(Model model,
		@PathVariable("date") final String date,
		@PathVariable("page") int page,
		@RequestParam(value = "search", required = false) String search) throws Exception {

		String decodedSearchInput = null;
		if (!Strings.isNullOrEmpty(search)) {
			decodedSearchInput = URLDecoder.decode(search, "UTF-8");
		}

		return getArticlesByDate(model, USCollectionService.SOCIETY_CACHE, date, ArticleSection.SOCIETY, "society", page, decodedSearchInput);
	}
	
	
	@RequestMapping(value = "/culture")
	public String getCulture(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.CULTURE_CACHE));
		model.addAttribute("menu","culture");
		return "/us-main";
	}
	
	@RequestMapping(value = "/culture/date/{date}/page/{page}")
	public String getCultureByDate(Model model,
		@PathVariable("date") final String date,
		@PathVariable("page") int page,
		@RequestParam(value = "search", required = false) String search) throws Exception {

		String decodedSearchInput = null;
		if (!Strings.isNullOrEmpty(search)) {
			decodedSearchInput = URLDecoder.decode(search, "UTF-8");
		}

		return getArticlesByDate(model, USCollectionService.CULTURE_CACHE, date, ArticleSection.CULTURE, "culture", page, decodedSearchInput);
	}
	
	@RequestMapping(value = "/ent")
	public String getEnt(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.ENT_CACHE));
		model.addAttribute("menu","ent");
		return "/us-main";
	}
	
	@RequestMapping(value = "/ent/date/{date}/page/{page}")
	public String getEntByDate(Model model,
		@PathVariable("date") final String date,
		@PathVariable("page") int page,
		@RequestParam(value = "search", required = false) String search) throws Exception {

		String decodedSearchInput = null;
		if (!Strings.isNullOrEmpty(search)) {
			decodedSearchInput = URLDecoder.decode(search, "UTF-8");
		}

		return getArticlesByDate(model, USCollectionService.ENT_CACHE, date, ArticleSection.ENT, "ent", page, decodedSearchInput);
	}

	@RequestMapping(value = "/sport")
	public String getSport(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.SPORT_CACHE));
		model.addAttribute("menu","sport");
		return "/us-main";
	}
	
	@RequestMapping(value = "/sport/date/{date}/page/{page}")
	public String getSportByDate(Model model,
		@PathVariable("date") final String date,
		@PathVariable("page") int page,
		@RequestParam(value = "search", required = false) String search) throws Exception {

		String decodedSearchInput = null;
		if (!Strings.isNullOrEmpty(search)) {
			decodedSearchInput = URLDecoder.decode(search, "UTF-8");
		}

		return getArticlesByDate(model, USCollectionService.SPORT_CACHE, date, ArticleSection.SPORT, "sport", page, decodedSearchInput);
	}

	@RequestMapping(value = "/it")
	public String getIt(Model model) throws Exception {
		getCommonInfo(model);
		model.addAttribute("articleMap", USCollectionService.getArticlesByCurrentTime(USCollectionService.IT_CACHE));
		model.addAttribute("menu","it");
		return "/us-main";
	}
	
	@RequestMapping(value = "/it/date/{date}/page/{page}")
	public String getITByDate(Model model,
		@PathVariable("date") final String date,
		@PathVariable("page") int page,
		@RequestParam(value = "search", required = false) String search) throws Exception {

		String decodedSearchInput = null;
		if (!Strings.isNullOrEmpty(search)) {
			decodedSearchInput = URLDecoder.decode(search, "UTF-8");
		}

		return getArticlesByDate(model, USCollectionService.IT_CACHE, date, ArticleSection.IT, "it", page, decodedSearchInput);
	}
}
