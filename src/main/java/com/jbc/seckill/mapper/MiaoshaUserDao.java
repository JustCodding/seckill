package com.jbc.seckill.mapper;

import com.jbc.seckill.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface MiaoshaUserDao {
	
	@Select("select * from seckill_user where id = #{id}")
	public MiaoshaUser getById(@Param("id") long id);
}
