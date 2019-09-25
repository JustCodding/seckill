package com.jbc.seckill.service;

import com.jbc.seckill.mapper.GoodsDao;
import com.jbc.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> getMiaoshaGoods(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getMiaoshaGoodByid(long id) {
        return goodsDao.getMiaoshaGoodByid(id);
    }

    public int reduceStock(GoodsVo goodsVo) {
        return goodsDao.reduceStock(goodsVo.getId());
    }

}
