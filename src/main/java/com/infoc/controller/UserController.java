package com.infoc.controller;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.infoc.domain.User;
import com.infoc.service.UserService;

@Controller
@RequestMapping("users")
public class UserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

//    @RequestMapping
//    public void getUsers(Pageable pageable, Model model) {
//        logger.debug("{}", ToStringBuilder.reflectionToString(pageable));
//        Page<User> page = userService.getUsers(pageable);
//        model.addAttribute("page", page);
//    }
//
//    @RequestMapping("{id}")
//    public String getUser(@PathVariable Long id, Model model) {
//        model.addAttribute(userService.getUser(id));
//        return "users/view";
//    }
//
//    @RequestMapping(value = "form", method = RequestMethod.GET)
//    public void getForm(@ModelAttribute User user, Model model) {
//        if (!user.isNew()) {
//            user = userService.getUser(user.getId());
//            model.addAttribute(user);
//        }
//    }
//
//    @RequestMapping(value = "form", method = RequestMethod.POST)
//    public String save(@ModelAttribute User user) {
//        userService.save(user);
//        return "redirect:" + user.getId();
//    }
//
//    @RequestMapping("delete")
//    public String delete(@RequestParam Long id) {
//        userService.delete(id);
//        return "redirect:/users";
//    }
}