package com.jbc.seckill.controller;

import com.jbc.seckill.domain.MiaoshaUser;
import com.jbc.seckill.result.Result;
import com.jbc.seckill.service.GoodsService;
import com.jbc.seckill.service.MiaoshaUserService;
import com.jbc.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/user")
public class UserController {

	private static Logger log = LoggerFactory.getLogger(UserController.class);
	

    @RequestMapping("/list")
    @ResponseBody
    public Result<MiaoshaUser> toList(Model model, MiaoshaUser user) {
        return Result.success(user);
    }

}
