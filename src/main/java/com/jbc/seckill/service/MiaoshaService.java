package com.jbc.seckill.service;

import com.jbc.seckill.domain.MiaoshaUser;
import com.jbc.seckill.domain.OrderInfo;
import com.jbc.seckill.mapper.GoodsDao;
import com.jbc.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MiaoshaService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo) {
        //减库存，下订单，写入秒杀订单
        goodsService.reduceStock(goodsVo);//减库存

        OrderInfo orderInfo = orderService.createOrder(user,goodsVo);//生成订单

        return orderInfo;
    }
}
