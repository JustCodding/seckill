package com.jbc.seckill.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.jbc.seckill.redis.RedisService;
import com.jbc.seckill.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/login")
public class LoginController {

	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	
	/*@Autowired
	MiaoshaUserService userService;*/
	
	@Autowired
    RedisService redisService;
	
    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }
    
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response) {
    	/*log.info(loginVo.toString());
    	//登录
    	userService.login(response, loginVo);
    	return Result.success(true);*/

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Result.success(true);
    }
}
