package com.jbc.seckill.controller;

import com.jbc.seckill.domain.MiaoshaUser;
import com.jbc.seckill.redis.GoodsKey;
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
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring4.context.SpringContextUtils;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
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

	@Autowired
	private RedisService redisService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

	
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

    //QTP:150/s  1000*10  页面缓存后QTP:615/s
    @RequestMapping(value="/to_list",produces = "text/html")
    @ResponseBody
    public String toList(HttpServletRequest request,HttpServletResponse response,Model model, MiaoshaUser user) {
        /*if(user==null){
            return "login";
        }*/

        //拿缓存页面
        String html = redisService.get(GoodsKey.goodsList,"",String.class);
        if(StringUtils.isNotEmpty(html)){
            return html;
        }

        List<GoodsVo> goodsList = goodsService.getMiaoshaGoods();
        model.addAttribute("goodsList",goodsList);
        model.addAttribute("user",user);

        //手动渲染
        WebContext wtx = new WebContext(request,response,request.getServletContext(),
                request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list",wtx);
        if(StringUtils.isNotEmpty(html)){
            redisService.set(GoodsKey.goodsList,"",html);
        }

        return html;
        //return "goods_list";
    }

    @RequestMapping(value="/to_detail/{id}",produces = "text/html")
    @ResponseBody
    public String toGoodDetail(HttpServletRequest request,HttpServletResponse response,Model model,MiaoshaUser user,@PathVariable("id") long id) {
        /*if(user==null){
            return "login";
        }*/
        //拿缓存页面
        String html = redisService.get(GoodsKey.goodsDetail,id+"",String.class);
        if(StringUtils.isNotEmpty(html)){
            return html;
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

        //手动渲染
        WebContext wtx = new WebContext(request,response,request.getServletContext(),
                request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail",wtx);
        if(StringUtils.isNotEmpty(html)){
            redisService.set(GoodsKey.goodsDetail,id+"",html);
        }
        return html;
        //return "goods_detail";
    }
    



}
