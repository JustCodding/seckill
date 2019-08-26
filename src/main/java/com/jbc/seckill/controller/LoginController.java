package com.jbc.seckill.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.jbc.seckill.redis.RedisService;
import com.jbc.seckill.result.CodeMsg;
import com.jbc.seckill.result.Result;
import com.jbc.seckill.service.MiaoshaUserService;
import com.jbc.seckill.utils.ValidatorUtil;
import com.jbc.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/login")
public class LoginController {

	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
    MiaoshaUserService userService;
	
	@Autowired
    RedisService redisService;
	
    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }
    
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(@Valid LoginVo loginVo, HttpServletResponse response) {

    	String mobile = loginVo.getMobile();
    	String password = loginVo.getPassword();
    	//交给参数校验
    	/*if(StringUtils.isEmpty(mobile)){
    	    return Result.error(CodeMsg.MOBILE_EMPTY);
        }
        if(StringUtils.isEmpty(password)){
            return Result.error(CodeMsg.PASSWORD_EMPTY);
        }
        if(!ValidatorUtil.isMobile(mobile)){
            return Result.error(CodeMsg.MOBILE_ERROR);
        }*/

        //登录
       userService.login(response,loginVo);
       return Result.success(true);
    }


}
