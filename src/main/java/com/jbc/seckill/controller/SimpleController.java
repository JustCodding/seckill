package com.jbc.seckill.controller;

import com.jbc.seckill.domain.User;
import com.jbc.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SimpleController {
    @Autowired
    private UserService userService;
    @RequestMapping("/hello")
    public String hello(){

        return "hello";
    }
    @RequestMapping("/user/{id}")
    @ResponseBody
    public User getUser(@PathVariable("id") int id){
        return userService.getUserByid(id);
    }
}
