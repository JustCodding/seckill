package com.jbc.seckill.service;

import com.jbc.seckill.domain.MiaoshaUser;
import com.jbc.seckill.exception.GlobalException;
import com.jbc.seckill.mapper.MiaoshaUserDao;
import com.jbc.seckill.redis.MiaoshaUserKey;
import com.jbc.seckill.redis.RedisService;
import com.jbc.seckill.result.CodeMsg;
import com.jbc.seckill.utils.MD5Util;
import com.jbc.seckill.utils.UUIDUtil;
import com.jbc.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class MiaoshaUserService {
	@Autowired
	private MiaoshaUserDao miaoshaUserDao;
	@Autowired
	private RedisService redisService;

	public static final String COOKIE_NAME_TOKEN = "token";

	public boolean login(HttpServletResponse response,LoginVo loginvo){
		MiaoshaUser user = miaoshaUserDao.getById(Long.valueOf(loginvo.getMobile()));
		if(user==null){
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}else {
			String formPass = MD5Util.formPassToDBPass(loginvo.getPassword(),user.getSalt());
			if(!formPass.equals(user.getPassword())){
				throw new GlobalException(CodeMsg.PASSWORD_ERROR);
			}
		}

		//登录验证成功  生成cookie
		String token = UUIDUtil.uuid();
		redisService.set(MiaoshaUserKey.token,token,user);
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);

		return true;
	}

	public MiaoshaUser getUserByToken(String token) {
		return redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);
	}
}
