package com.sym.miaoshaodemo.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sym.miaoshaodemo.controller.LoginController;
import com.sym.miaoshaodemo.result.Result;
import com.sym.miaoshaodemo.util.MD5Util;
import com.sym.miaoshaodemo.vo.LoginVo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: LoginTest
 * @Package com.sym.miaoshaodemo.login
 * @Description: TODO
 * @date 2019/5/4 16:25
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginTest {

    @Autowired
    private TestRestTemplate restTemplate;


    private LoginController loginController;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Before
    public void setUp(){
        loginController = new LoginController();
    }

    @Test
    public void doLogin(){
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("mobile","15914343735");
        map.add("password", MD5Util.inputPassToFormPass("111111"));
        ResponseEntity<String> result = restTemplate.postForEntity("/login/do_login",map,String.class);
        JSONObject jsonObject = JSON.parseObject(result.getBody());
//        Assert.assertArrayEquals(true);jsonObject.get("data");
        System.out.println(jsonObject.get("data"));
    }

    @Test
    public void sendMessage(){
        amqpTemplate.convertAndSend("myTopic","aaa.3","hello msg");
        System.out.println("end");
    }
}
