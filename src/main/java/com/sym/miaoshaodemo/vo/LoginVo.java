package com.sym.miaoshaodemo.vo;

import com.sym.miaoshaodemo.validator.IsMobile;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: LoginVO
 * @Package com.sym.miaoshaodemo.vo
 * @Description: 登录传输参数实体类
 * @date 2019/4/28 22:05
 */
public class LoginVo {

    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min=32)
    private String password;

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "LoginVo [mobile=" + mobile + ", password=" + password + "]";
    }
}