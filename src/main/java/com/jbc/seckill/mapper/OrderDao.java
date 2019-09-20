package com.jbc.seckill.mapper;

import com.jbc.seckill.domain.MiaoshaOrder;
import com.jbc.seckill.domain.OrderInfo;
import com.jbc.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface OrderDao {

	@Select("select * from miaosha_order where user_id=#{userid} and goods_id=#{goodsId}")
	MiaoshaOrder getMiaoshaOrder(@Param("userid") long userid, @Param("goodsId")long goodsId);
	@Insert("insert into order_info(user_id,goods_id,goods_name,goods_count,goods_price,order_channel,status,create_date)values(#{userId},#{goodsId},#{goodsName},#{goodsCount},#{goodsPrice},#{orderChannel},#{status},#{createDate})")
	@SelectKey(keyColumn = "id",keyProperty = "id",resultType = long.class,before = false,statement = "select last_insert_id()")
    long createOrder(OrderInfo order);
	@Insert("insert into miaosha_order(user_id,order_id,goods_id)values(#{userId},#{orderId},#{goodsId})")
	void createMiaoshaOrder(MiaoshaOrder miaoshaOrder);
}
