package com.infoc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class HomeController extends BaseController {
	
	@RequestMapping(method=RequestMethod.GET, value="/home")
	public String home() {
		return "/home";
	}
	
}
