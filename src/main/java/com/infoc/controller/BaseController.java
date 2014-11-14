package com.infoc.controller;

import com.infoc.domain.Article;
import com.infoc.service.ArticleService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Throwables;

import java.util.HashMap;
import java.util.Map;

@Controller
public class BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    ArticleService articleService;

	/**
	 * ConversionNotSupportedException:	500 (Internal Server Error)
	 * HttpMediaTypeNotAcceptableException:	406 (Not Acceptable)
	 * HttpMediaTypeNotSupportedException:	415 (Unsupported Media Type)
	 * HttpMessageNotReadableException:	400 (Bad Request)
	 * HttpMessageNotWritableException:	500 (Internal Server Error)
	 * HttpRequestMethodNotSupportedException:	405 (Method Not Allowed)
	 * MissingServletRequestParameterException:	400 (Bad Request)
	 * NoSuchRequestHandlingMethodException:	404 (Not Found)
	 * TypeMismatchException:	400 (Bad Request)
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView handleProjectException(Exception e) {
		e.printStackTrace();
		
		return new ModelAndView("common/exception")
			.addObject("exception", e.getClass().getName())
			.addObject("errMsg", Throwables.getRootCause(e).getMessage());
	}

    protected void setCommonInfo(Model model) {
        DateTime dTime = new DateTime(DateTimeZone.forID("Asia/Seoul"));
        model.addAttribute("initDay", dTime.toString(DateTimeFormat.forPattern("MM/dd/yyyy hh:mm:ss")));
        model.addAttribute("currentDay", dTime.toString(DateTimeFormat.forPattern("yyyy.MM.dd")));
        model.addAttribute("requestDay", dTime.toString(DateTimeFormat.forPattern("yyyy-dd-MM")));
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
