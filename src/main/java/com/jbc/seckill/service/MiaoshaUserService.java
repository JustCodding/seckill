package com.jbc.seckill.service;

import com.jbc.seckill.domain.MiaoshaUser;
import com.jbc.seckill.exception.GlobalException;
import com.jbc.seckill.mapper.MiaoshaUserDao;
import com.jbc.seckill.result.CodeMsg;
import com.jbc.seckill.utils.MD5Util;
import com.jbc.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MiaoshaUserService {
	@Autowired
	private MiaoshaUserDao miaoshaUserDao;
	public boolean login(LoginVo loginvo){
		MiaoshaUser user = miaoshaUserDao.getById(Long.valueOf(loginvo.getMobile()));
		if(user==null){
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}else {
			String formPass = MD5Util.formPassToDBPass(loginvo.getPassword(),user.getSalt());
			if(!formPass.equals(user.getPassword())){
				throw new GlobalException(CodeMsg.PASSWORD_ERROR);
			}
		}

		return true;
	}

}
