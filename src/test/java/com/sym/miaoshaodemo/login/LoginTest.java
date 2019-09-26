package com.sym.miaoshaodemo.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sym.miaoshaodemo.controller.LoginController;
import com.sym.miaoshaodemo.domain.Goods;
import com.sym.miaoshaodemo.domain.OrderInfo;
import com.sym.miaoshaodemo.redis.config.RedisUtil;
import com.sym.miaoshaodemo.result.Result;
import com.sym.miaoshaodemo.service.TestCacheService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Autowired
    private RedisUtil redisUtil;


    private LoginController loginController;

    @Autowired
    private TestCacheService testCacheService;

    @Autowired
    private AmqpTemplate amqpTemplate;

//    @Before
    public void setUp(){
        loginController = new LoginController();
    }

//    @Test
    public void doLogin(){
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("mobile","15914343735");
        map.add("password", MD5Util.inputPassToFormPass("111111"));
        ResponseEntity<String> result = restTemplate.postForEntity("/login/do_login",map,String.class);
        JSONObject jsonObject = JSON.parseObject(result.getBody());
//        Assert.assertArrayEquals(true);jsonObject.get("data");
        System.out.println(jsonObject.get("data"));
    }

//    @Test
    public void sendMessage(){
        amqpTemplate.convertAndSend("myTopic","aaa.3","hello msg");
        System.out.println("end");
    }

    @Test
    public void testCache(){
        System.out.println(testCacheService.testMethod2());
    }

    @Test
    public void testCache2(){
        testCacheService.testPut("10086");
    }

    @Test
    public void testCache3(){
        testCacheService.testOrder(100112);
    }

    @Test
    public void testHMSET(){
        Map<String,String> studentMap = new HashMap<>();
        Goods goods = new Goods();
        goods.setId(1);
        goods.setGoodsName("测试商品");

        Map<String,String> goodMap = (Map<String, String>) goods;

        studentMap.put("id","1");
        studentMap.put("name","corn");
        studentMap.put("age","26");
        studentMap.put("set","0");
        studentMap.put("birth","1993");

//        System.out.println(redisUtil.hmset("Hash:Student","2",goodMap));
        Map<String,String> result = redisUtil.hgetall("Hash:Student","1");
        System.out.println(1);
    }

    @Test
    public void testLpush(){
        Goods goods = new Goods();
        goods.setId(5);
        goods.setGoodsName("测试商品5");
        System.out.println(redisUtil.lpush("good","10086",goods));
    }

    @Test
    public void testLindex(){
        Goods goods = redisUtil.lindex("good","10086",1L,Goods.class);
        System.out.println(111);
    }

    @Test
    public void testLrange(){
//        List<Goods> list = redisUtil.lrange("good","10086",0L,10L,Goods.class);
        Map<Object,Object> result = redisUtil.lrangeByPage("good","10086",3L,2L,Goods.class);
        System.out.println(1);
    }

    @Test
    public void testSortedSet(){
        Map<String,Double> data = new HashMap<>();
        data.put("corn",89d);
        data.put("tom",99d);
        data.put("lily",55d);
        data.put("bill",67d);
        data.put("bob",34d);
        data.put("ben",100d);
        data.put("tim",77d);
        data.put("suyim",89d);
        data.put("ice",85d);
        data.put("ocen",95d);
        System.out.println("init data"+redisUtil.zadd("score:math",null,data));
    }

    @Test
    public void testSortedSet2(){
        Set<String> result = redisUtil.zrevrangeByScore("score:math",null,100L,0L);
        System.out.println(1);
    }
}

