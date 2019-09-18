package com.jbc.seckill.controller;

import com.jbc.seckill.domain.MiaoshaOrder;
import com.jbc.seckill.domain.MiaoshaUser;
import com.jbc.seckill.domain.OrderInfo;
import com.jbc.seckill.result.CodeMsg;
import com.jbc.seckill.service.GoodsService;
import com.jbc.seckill.service.MiaoshaService;
import com.jbc.seckill.service.MiaoshaUserService;
import com.jbc.seckill.service.OrderService;
import com.jbc.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

	private static Logger log = LoggerFactory.getLogger(MiaoshaController.class);
	
	@Autowired
    MiaoshaUserService userService;

	@Autowired
	private GoodsService goodsService;

    @Autowired
	private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;






    @RequestMapping("/do_miaosha")
    public String miaosha(Model model,MiaoshaUser user,long goodsId) {
        if(user==null){
            return "login";
        }
        //判断库存
        GoodsVo goodsVo = goodsService.getMiaoshaGoodByid(goodsId);
        if(goodsVo.getStockCount()<=0){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrder(user.getId(),goodsId);
        if(order!=null){
            model.addAttribute("errmsg", CodeMsg.MIAO_SHA_REPEATE.getMsg());
            return "miaosha_fail";
        }

        //减库存，下订单，写入秒杀订单(要在一个事务中)
        OrderInfo orderinfo = miaoshaService.miaosha(user,goodsVo);
        model.addAttribute("orderinfo",orderinfo);
        model.addAttribute("goods",goodsVo);
        model.addAttribute("user",user);
        return "order_detail";
    }
}