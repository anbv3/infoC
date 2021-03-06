package com.infoc.controller;

import com.google.common.base.Strings;
import com.infoc.common.Constant;
import com.infoc.domain.Article;
import com.infoc.enumeration.ArticleSection;
import com.infoc.service.ArticleService;
import com.infoc.service.CollectionService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/kr")
public class KRNewsController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(KRNewsController.class);
    private static final String SUM = "요약";
    private static final String CONTENTS = "원문 내용";
    private static final String MORE = "출처";
    private static final String RELATED = "관련 기사";

	@Autowired
	ArticleService articleService;

    private void setLocaleWords(Model model) {
        model.addAttribute("summary", SUM);
        model.addAttribute("contents", CONTENTS);
        model.addAttribute("more", MORE);
        model.addAttribute("related", RELATED);
    }

	public String getArticlesByDate(Model model, Map<Integer, List<Article>> cacheMap, final String date,
		ArticleSection section, String menuName, int page, String search) throws Exception {

		DateTime currTime = new DateTime(DateTimeZone.forID("Asia/Seoul"));
		DateTime reqTime = new DateTime(Long.parseLong(date), DateTimeZone.forID("Asia/Seoul"));

		Map<Integer, List<Article>> articleListMap = new HashMap<Integer, List<Article>>();

		if (reqTime.getDayOfMonth() == currTime.getDayOfMonth()) {
			articleListMap = CollectionService.getArticlesByCurrentTime(cacheMap, page, search);
			model.addAttribute("end", false);
		} else {
			// 전날 "뉴스" 가져오기
			Map<Integer, List<Article>> oldArticleListMap =
				articleService.getArticlesByPubDateAndSection(reqTime.toDate(), "KR", section);

			if (oldArticleListMap == null || oldArticleListMap.isEmpty()) {
				model.addAttribute("end", true);
			} else {
				articleListMap = CollectionService.getArticlesByPage(oldArticleListMap, page, search);
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

        setLocaleWords(model);
		return "/common/articles";
	}

    @RequestMapping
    public String main() {
        return "redirect:/kr/main";
    }

    @RequestMapping(value = "/{section}")
    public String getNews(Model model,
    		@PathVariable("section") final String section,
    		@RequestParam(value = "q", required = false) String query) throws Exception {
    	LOG.debug("section:{}, query: {}", section, query);

        if (Strings.isNullOrEmpty(query)) {
        	model.addAttribute("articleMap",
        			CollectionService.getArticlesByCurrentTime(ArticleSection.findKRCache(section)));
        } else {
        	Page<Article> articleList = articleService.getArticlesByMainContents(
        			"KR", ArticleSection.find(section), query, new PageRequest(0, Constant.PAGE_SIZE.getVal()));
    		model.addAttribute("page", articleList);
    		model.addAttribute("pubDate", articleService.extractStartAndEndDate(articleList));
    		model.addAttribute("query", query);
        }

        model.addAttribute("menu", section);

        setCommonInfo(model);
        setLocaleWords(model);
        return "/main";
    }

    @RequestMapping(value = "/{section}/date/{date}/page/{page}")
    public String getNewsByDate(Model model,
                                @PathVariable("section") final String section,
                                @PathVariable("date") final String date,
                                @PathVariable("page") int page,
                                @RequestParam(value = "q", required = false) String query) throws Exception {

        String decodedSearchInput = null;
        if (!Strings.isNullOrEmpty(query)) {
            decodedSearchInput = URLDecoder.decode(query, "UTF-8");
        }

        return getArticlesByDate(model, ArticleSection.findKRCache(section), date, ArticleSection.find(section), section, page, decodedSearchInput);
    }

	@RequestMapping(value = "/search/{section}")
	public String getMainBySearch(Pageable pageable, Model model,
			@PathVariable("section") final String section,
			@RequestParam(value = "q") String query) throws Exception {
		LOG.debug("section:{}, query: {}, {}", section, query, ToStringBuilder.reflectionToString(pageable));

        String decodedQuery = null;
        if (!Strings.isNullOrEmpty(query)) {
            decodedQuery = URLDecoder.decode(query, "UTF-8");
        }

		Page<Article> articleList = articleService.getArticlesByMainContents("KR", ArticleSection.find(section), decodedQuery, pageable);
		model.addAttribute("page", articleList);
		model.addAttribute("pubDate", articleService.extractStartAndEndDate(articleList));
		model.addAttribute("menu", section);

        setLocaleWords(model);
		return "/common/searched-articles";
	}

    @RequestMapping(value = "/article/{articleId}")
    @ResponseBody
    public Map<String, String> getArticleContents(@PathVariable("articleId") final Long articleId) {
        LOG.debug("articleId: {}", articleId);

        Map<String, String> json = new HashMap<>();
        try {
            Article article = articleService.getArticle(articleId);

            StringBuilder text = new StringBuilder();
            if (article.getContents().isEmpty()) {
                text.append("No data for the old articles.");
            } else {
                text.append(article.getContents().trim());
            }

            text.append("<br><strong>")
                .append("Source: ")
                .append(article.getAuthor().trim())
                .append("</strong>");

            json.put("status", "SUCCESS");
            json.put("contents", text.toString());
        } catch (Exception e) {
            json.put("status", "FAILED");
        }

        return json;
    }
}
