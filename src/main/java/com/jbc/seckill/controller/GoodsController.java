package com.jbc.seckill.controller;

import com.jbc.seckill.domain.MiaoshaUser;
import com.jbc.seckill.redis.MiaoshaUserKey;
import com.jbc.seckill.redis.RedisService;
import com.jbc.seckill.result.Result;
import com.jbc.seckill.service.MiaoshaUserService;
import com.jbc.seckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/goods")
public class GoodsController {

	private static Logger log = LoggerFactory.getLogger(GoodsController.class);
	
	@Autowired
    MiaoshaUserService userService;


	
    /*@RequestMapping("/to_list")
    //从cookie中取token,为兼容移动端(从请求中传过来)增加从请求参数中农获取token
    public String toLogin(Model model,@CookieValue(value = MiaoshaUserService.COOKIE_NAME_TOKEN,required = false) String cookieToken,
                          @RequestParam(value = MiaoshaUserService.COOKIE_NAME_TOKEN,required = false) String paramToken,HttpServletResponse response) {
        if(StringUtils.isEmpty(cookieToken)&&StringUtils.isEmpty(paramToken)){
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:cookieToken;
        MiaoshaUser user = userService.getUserByToken(token,response);
        model.addAttribute("user",user);
        return "goods_list";
    }*/

    @RequestMapping("/to_list")
    public String toLogin(Model model,MiaoshaUser user) {
        if(user==null){
            return "login";
        }
        model.addAttribute("user",user);
        return "goods_list";
    }
    



}
