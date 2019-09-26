package com.jbc.seckill.controller;

import com.jbc.seckill.domain.MiaoshaOrder;
import com.jbc.seckill.domain.MiaoshaUser;
import com.jbc.seckill.domain.OrderInfo;
import com.jbc.seckill.rabbitmq.MQSender;
import com.jbc.seckill.rabbitmq.MiaoshaMessage;
import com.jbc.seckill.redis.GoodsKey;
import com.jbc.seckill.redis.RedisService;
import com.jbc.seckill.result.CodeMsg;
import com.jbc.seckill.result.Result;
import com.jbc.seckill.service.GoodsService;
import com.jbc.seckill.service.MiaoshaService;
import com.jbc.seckill.service.MiaoshaUserService;
import com.jbc.seckill.service.OrderService;
import com.jbc.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean{
    private Map<Long,Boolean> localOverMap = new HashMap<>();

    //将秒杀商品库存预先加载到redis缓存中
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> miaoshaoGooods = goodsService.getMiaoshaGoods();
        if(miaoshaoGooods!=null||miaoshaoGooods.size()>0){
            for (GoodsVo goodsVo:miaoshaoGooods) {
                redisService.set(GoodsKey.miaoshaogoodstock,""+goodsVo.getId(),goodsVo.getStockCount());
                localOverMap.put(goodsVo.getId(),false);
            }
        }
    }

	private static Logger log = LoggerFactory.getLogger(MiaoshaController.class);
	
	@Autowired
    MiaoshaUserService userService;

	@Autowired
	private GoodsService goodsService;

    @Autowired
	private OrderService orderService;

    @Autowired
    private MiaoshaService miaoshaService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender sender;


    //QTP:178/s  1000*10
    @RequestMapping(value="/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result miaosha(Model model, MiaoshaUser user, long goodsId) {
        if(user==null){
            return Result.error(CodeMsg.USER_NOT_LOGIN);
        }
        //先判断内存标记 秒杀抢完则不用再去访问redis,直接返回
        if(localOverMap.get(goodsId)){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //在redis中减库存
        long num = redisService.decr(GoodsKey.miaoshaogoodstock,""+goodsId);
        if(num<0){//说明秒杀完了
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //判断是否秒杀过了
        MiaoshaOrder order = orderService.getMiaoshaOrder(user.getId(),goodsId);
        if(order!=null){
            return Result.error(CodeMsg.MIAO_SHA_REPEATE);
        }

        //添加到队列中
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setGoodsId(goodsId);
        miaoshaMessage.setUser(user);
        sender.sendMiaoshaMessage(miaoshaMessage);

        return Result.success(0);


    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model,MiaoshaUser user, @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaOrder(user.getId(), goodsId);
        return Result.success(result);
    }


    //QTP:178/s  1000*10
    @RequestMapping("/do_miaosha0")
    @ResponseBody
    public Result miaosha0(Model model, MiaoshaUser user, long goodsId) {
        if(user==null){
            return Result.error(CodeMsg.USER_NOT_LOGIN);
        }
        //判断库存
        GoodsVo goodsVo = goodsService.getMiaoshaGoodByid(goodsId);
        if(goodsVo.getStockCount()<=0){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrder(user.getId(),goodsId);
        if(order!=null){
            return Result.error(CodeMsg.MIAO_SHA_REPEATE);
        }

        //减库存，下订单，写入秒杀订单(要在一个事务中)
        OrderInfo orderinfo = miaoshaService.miaosha(user,goodsVo);
        /*model.addAttribute("orderInfo",orderinfo);
        model.addAttribute("goods",goodsVo);
        model.addAttribute("user",user);*/
        if(orderinfo==null){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        return Result.success(orderinfo);
    }

    //QTP:178/s  1000*10
    @RequestMapping("/do_miaosha1")
    public String miaosha1(Model model,MiaoshaUser user,long goodsId) {
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
        model.addAttribute("orderInfo",orderinfo);
        model.addAttribute("goods",goodsVo);
        model.addAttribute("user",user);
        return "order_detail";
    }

}
