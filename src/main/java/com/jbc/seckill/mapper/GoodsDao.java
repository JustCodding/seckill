package com.jbc.seckill.mapper;

import java.util.List;

import com.jbc.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;



@Mapper
public interface GoodsDao {
	
	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
	public List<GoodsVo> listGoodsVo();

	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where mg.goods_id=#{id} ")
    GoodsVo getMiaoshaGoodByid(long id);

	@Update("update miaosha_goods set stock_count=stock_count-1 where goods_id=#{id} and stock_count>0")
	int reduceStock(Long id);
}
