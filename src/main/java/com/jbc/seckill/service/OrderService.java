package com.jbc.seckill.service;

import com.jbc.seckill.domain.MiaoshaOrder;
import com.jbc.seckill.domain.MiaoshaUser;
import com.jbc.seckill.domain.OrderInfo;
import com.jbc.seckill.mapper.GoodsDao;
import com.jbc.seckill.mapper.OrderDao;
import com.jbc.seckill.redis.OrderKey;
import com.jbc.seckill.redis.RedisService;
import com.jbc.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private OrderDao orderdao;

    @Autowired
    private RedisService redisService;

    public MiaoshaOrder getMiaoshaOrder(long userid, long goodsId) {
        return redisService.get(OrderKey.getOrderByid,userid+"_"+goodsId,MiaoshaOrder.class);
        //return orderdao.getMiaoshaOrder(userid,goodsId);
    }

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goodsVo) {
        OrderInfo order = new OrderInfo();
        order.setCreateDate(new Date());
        order.setDeliveryAddrId(1L);
        order.setGoodsCount(1);
        order.setGoodsId(goodsVo.getId());
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsPrice(goodsVo.getMiaoshaPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setUserId(user.getId());
        orderdao.createOrder(order);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setOrderId(order.getId());
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrder.setUserId(user.getId());
        orderdao.createMiaoshaOrder(miaoshaOrder);

        redisService.set(OrderKey.getOrderByid,user.getId()+"_"+goodsVo.getId(),miaoshaOrder);

        return order;

    }

    public OrderInfo getOrderById(long orderid){
        return orderdao.getOrderById(orderid);
    }

}
