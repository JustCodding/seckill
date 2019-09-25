package com.jbc.seckill.controller;

import com.jbc.seckill.domain.MiaoshaUser;
import com.jbc.seckill.domain.OrderInfo;
import com.jbc.seckill.redis.GoodsKey;
import com.jbc.seckill.redis.RedisService;
import com.jbc.seckill.result.CodeMsg;
import com.jbc.seckill.result.Result;
import com.jbc.seckill.service.GoodsService;
import com.jbc.seckill.service.MiaoshaUserService;
import com.jbc.seckill.service.OrderService;
import com.jbc.seckill.vo.GoodsDetailVo;
import com.jbc.seckill.vo.GoodsVo;
import com.jbc.seckill.vo.OrderDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping("/order")
public class OrderController {

	private static Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
    MiaoshaUserService userService;

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private RedisService redisService;

    @Autowired
    private OrderService orderService;


    //采用页面静态化，动静分离，不是返回整个页面，而只是返回页面需要的数据
    @RequestMapping(value="/detail")
    @ResponseBody
    public Result<OrderDetailVo> detail(Model model, MiaoshaUser user, long orderId) {
        if(user==null){
            return Result.error(CodeMsg.USER_NOT_LOGIN);
        }

        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if(orderInfo==null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        GoodsVo goodsVo = goodsService.getMiaoshaGoodByid(orderInfo.getGoodsId());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(orderInfo);
        orderDetailVo.setGoods(goodsVo);
        return  Result.success(orderDetailVo);
    }



}
