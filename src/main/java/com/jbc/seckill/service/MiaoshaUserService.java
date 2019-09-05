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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		String token = UUIDUtil.uuid();
		//登录验证成功  生成cookie
		addCookie(response,token, user);
		return true;
	}

	private void addCookie(HttpServletResponse response,String token, MiaoshaUser user) {
		//String token = UUIDUtil.uuid();
		redisService.set(MiaoshaUserKey.token,token,user);
		Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	public MiaoshaUser getUserByToken(String token,HttpServletResponse response) {
		if(StringUtils.isEmpty(token)){
			return null;
		}
		MiaoshaUser user =  redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);
		//重新设置cookie 这样就相当于延长了cookie的有效期
		if(user!=null){
			//redisService.removeKey(MiaoshaUserKey.token,token);
			addCookie(response,token,user);
		}

		return user;
	}
}
