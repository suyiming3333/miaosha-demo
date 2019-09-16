package com.sym.miaoshaodemo.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sym.miaoshaodemo.domain.MiaoshaUser;
import com.sym.miaoshaodemo.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * �Զ���������������
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	MiaoshaUserService userService;


	/**
	 * @description: ������������ƥ�䣬�Ž��лص�����
	 * @params
	 * @param parameter
	 * @return boolean
	 * @throws
	 * @author suyiming3333
	 * @date 2019/5/4 15:29
	 */
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> clazz = parameter.getParameterType();
		return clazz== MiaoshaUser.class;
	}

	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		
		String paramToken = request.getParameter(MiaoshaUserService.COOKI_NAME_TOKEN);
		String cookieToken = getCookieValue(request, MiaoshaUserService.COOKI_NAME_TOKEN);
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
			return null;
		}
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return userService.getByToken(response, token);
	}

	/**
	 * @description: ����cookies����
	 * @params
	 * @param request
	 * @param cookiName
	 * @return java.lang.String
	 * @throws
	 * @author suyiming3333
	 * @date 2019/5/4 14:55
	 */
	private String getCookieValue(HttpServletRequest request, String cookiName) {
		Cookie[]  cookies = request.getCookies();

		if(cookies == null || cookies.length<0){
			return null;
		}
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals(cookiName)) {
				return cookie.getValue();
			}
		}
		return null;
	}

}
