package com.jbc.seckill.service;

import com.jbc.seckill.domain.MiaoshaOrder;
import com.jbc.seckill.domain.MiaoshaUser;
import com.jbc.seckill.domain.OrderInfo;
import com.jbc.seckill.mapper.GoodsDao;
import com.jbc.seckill.redis.MiaoshaKey;
import com.jbc.seckill.redis.OrderKey;
import com.jbc.seckill.redis.RedisService;
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
    @Autowired
    private RedisService redisService;
    @Autowired
    MiaoshaService miaoshaService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo) {
        //减库存，下订单，写入秒杀订单
        int index = goodsService.reduceStock(goodsVo);//减库存
        OrderInfo orderInfo = null;
        if(index>0){
            orderInfo = orderService.createOrder(user,goodsVo);//生成订单
        }else{//减库存失败 说明没有了
            miaoshaService.setGoodsOver(goodsVo.getId());
        }

        return orderInfo;
    }

    public long getMiaoshaOrder(Long id, long goodsId) {
        MiaoshaOrder order = orderService.getMiaoshaOrder(id,goodsId);
        if(order!=null){
            return order.getOrderId();
        }else{
            if(getGoodsOver(goodsId)){
                return -1;
            }else{
                return 0;
            }
        }

    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, ""+goodsId);
    }
}
