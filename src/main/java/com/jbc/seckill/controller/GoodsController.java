package com.jbc.seckill.controller;

import com.jbc.seckill.domain.MiaoshaUser;
import com.jbc.seckill.redis.MiaoshaUserKey;
import com.jbc.seckill.redis.RedisService;
import com.jbc.seckill.result.Result;
import com.jbc.seckill.service.GoodsService;
import com.jbc.seckill.service.MiaoshaUserService;
import com.jbc.seckill.vo.GoodsVo;
import com.jbc.seckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/goods")
public class GoodsController {

	private static Logger log = LoggerFactory.getLogger(GoodsController.class);
	
	@Autowired
    MiaoshaUserService userService;

	@Autowired
	private GoodsService goodsService;


	
    /*@RequestMapping("/to_list")
    //从cookie中取token,为兼容移动端(从请求中传过来)增加从请求参数中农获取token
    public String toList(Model model,@CookieValue(value = MiaoshaUserService.COOKIE_NAME_TOKEN,required = false) String cookieToken,
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
    public String toList(Model model,MiaoshaUser user) {
        if(user==null){
            return "login";
        }

        List<GoodsVo> goodsList = goodsService.getMiaoshaGoods();

        model.addAttribute("goodsList",goodsList);
        model.addAttribute("user",user);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{id}")
    public String toGoodDetail(Model model,MiaoshaUser user,@PathVariable("id") long id) {
        if(user==null){
            return "login";
        }

        GoodsVo goods = goodsService.getMiaoshaGoodByid(id);
        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now<startAt){//秒杀还没开始
            remainSeconds = (int)(startAt-now)/1000;
        }else if(now>endAt){//秒杀已经结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("goods",goods);
        model.addAttribute("user",user);
        model.addAttribute("miaoshaStatus",miaoshaStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        return "goods_detail";
    }
    



}
