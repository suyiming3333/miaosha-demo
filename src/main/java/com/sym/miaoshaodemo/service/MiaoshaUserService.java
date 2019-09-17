package com.sym.miaoshaodemo.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.sym.miaoshaodemo.dao.MiaoshaUserDao;
import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.domain.User;
import com.sym.miaoshaodemo.exception.GlobalException;
import com.sym.miaoshaodemo.redis.key.MiaoshaUserKey;
import com.sym.miaoshaodemo.redis.key.UserKey;
import com.sym.miaoshaodemo.redis.service.RedisService;
import com.sym.miaoshaodemo.result.CodeMsg;
import com.sym.miaoshaodemo.util.MD5Util;
import com.sym.miaoshaodemo.util.UUIDUtil;
import com.sym.miaoshaodemo.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("userService")
public class MiaoshaUserService {
	
	
	public static final String COOKI_NAME_TOKEN = "token";
	
	@Autowired
	MiaoshaUserDao miaoshaUserDao;
	
	@Autowired
	RedisService redisService;
	
	public MiaoshaUser getByMobile(long mobile) {
		return miaoshaUserDao.getByMobile(mobile);
	}

    /**
     * @description: 从缓存中获取用户信息　　
     * @params
     * @param response
     * @param token
     * @return com.sym.miaoshaodemo.domain.MiaoshaUser
     * @throws
     * @author suyiming3333
     * @date 2019/5/4 14:57
     */
	public MiaoshaUser getByToken(HttpServletResponse response, String token) {

		if(StringUtils.isEmpty(token)) {
			return null;
		}
		MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
		//延长有效期
		if(user != null) {
			addCookie(response, token, user);
		}
		return user;
	}
	

	public boolean login(HttpServletResponse response, LoginVo loginVo) {
		if(loginVo == null) {
			//直接抛出业务异常
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		//判断手机号是否存在
		//先从缓存里判断改手机号是否有登陆过
		MiaoshaUser user = redisService.get(UserKey.getGetByMobile,mobile,MiaoshaUser.class);
		String dbPass = null;
		String saltDB = null;
		String calcPass = null;
		String token = null;
		if(user !=null){
			//从缓存里面做登录逻辑校验
			dbPass = user.getPassword();
			saltDB = user.getSalt();
			calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
			//校验密码
			if(!calcPass.equals(dbPass)) {
				//密码不对
				throw new GlobalException(CodeMsg.PASSWORD_ERROR);
			}
			//设置cookie、redis保存token-user
			addCookie(response, token, user);
			System.out.println("load user from redis");
			return true;
		}else{
			//缓存取不到，查一次数据库
			user = getByMobile(Long.parseLong(mobile));
			if(user == null) {
				throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
			}
			//验证密码
			dbPass = user.getPassword();
			saltDB = user.getSalt();
			calcPass = MD5Util.formPassToDBPass(formPass, saltDB);

			//登陆成功后，生成cookie给前端
			token = UUIDUtil.uuid();
			/**添加cookie，将token与用户信息绑定放到redis,用于做session同步**/
			addCookie(response, token, user);
			System.out.println("load user from db");
			return true;
		}
	}

	public String login2(HttpServletResponse response, LoginVo loginVo) {
		if(loginVo == null) {
			//直接抛出业务异常
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		String mobile = loginVo.getMobile();
		String formPass = loginVo.getPassword();
		//判断手机号是否存在
		MiaoshaUser user = getByMobile(Long.parseLong(mobile));
		if(user == null) {
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		}
		//验证密码
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
		String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		//登陆成功后，生成cookie给前端
		String token = UUIDUtil.uuid();
		/**添加cookie，将token与用户信息绑定放到redis,用于做session同步**/
		addCookie(response, token, user);
		return token;
	}

	/**
	 * @description: 添加或刷新cookies　　
	 * @params
	 * @param response
	 * @param token
	 * @param user
	 * @return void
	 * @throws
	 * @author suyiming3333
	 * @date 2019/5/4 15:54
	 */
	private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
		//token-user存入redis
		redisService.set(MiaoshaUserKey.token, token, user);
		//mobile-user存入redis
		redisService.set(UserKey.getGetByMobile,user.getMobile().toString(),user);
		//创建cookies
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		//设置cookies的超时时间
		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
		//设置cookies的有效路径
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
