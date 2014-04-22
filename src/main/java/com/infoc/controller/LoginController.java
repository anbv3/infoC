package com.infoc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infoc.service.UserService;

@Controller
@RequestMapping("login")
public class LoginController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @RequestMapping
    public String getLogin() {
        return "/login";
    }
    
}