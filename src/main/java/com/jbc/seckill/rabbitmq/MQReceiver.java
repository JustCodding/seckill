package com.jbc.seckill.rabbitmq;

import com.jbc.seckill.domain.MiaoshaOrder;
import com.jbc.seckill.domain.MiaoshaUser;
import com.jbc.seckill.domain.OrderInfo;
import com.jbc.seckill.redis.RedisService;
import com.jbc.seckill.service.GoodsService;
import com.jbc.seckill.service.MiaoshaService;
import com.jbc.seckill.service.OrderService;
import com.jbc.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class MQReceiver {

		private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
		@Autowired
		RedisService redisService;
		@Autowired
		MiaoshaService miaoshaService;

		@Autowired
		private GoodsService goodsService;

		@Autowired
		private OrderService orderService;



		@RabbitListener(queues=MQConfig.MIAOSHA_QUEUE )
		public void  receiveMiaoshaMsg(String message){
			log.info("receive  message:"+message);
			MiaoshaMessage miaoshaMessage = redisService.stringToBean(message,MiaoshaMessage.class);
			MiaoshaUser user = miaoshaMessage.getUser();
			long goodsId = miaoshaMessage.getGoodsId();

			//判断库存
			GoodsVo goods = goodsService.getMiaoshaGoodByid(goodsId);
			int stock = goods.getStockCount();
			if(stock <= 0) {
				return;
			}
			//判断是否已经秒杀到了
			MiaoshaOrder order = orderService.getMiaoshaOrder(user.getId(), goodsId);
			if(order != null) {
				return;
			}
			//减库存 下订单 写入秒杀订单
			miaoshaService.miaosha(user, goods);

		}

		@RabbitListener(queues=MQConfig.QUEUE)
		public void receive(String message) {
			log.info("receive message:"+message);
		}

		@RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
		public void receiveTopic1(String message) {
			log.info(" topic  queue1 message:"+message);
		}

		@RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
		public void receiveTopic2(String message) {
			log.info(" topic  queue2 message:"+message);
		}

		@RabbitListener(queues=MQConfig.HEADER_QUEUE)
		public void receiveHeaderQueue(byte[] message) {
			log.info(" header  queue message:"+new String(message));
		}

		
}
