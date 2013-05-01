package com.infoc.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Throwables;


public class BaseController {

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
			.addObject("ERR_MSG", Throwables.getRootCause(e).getMessage());
	}
	
}
