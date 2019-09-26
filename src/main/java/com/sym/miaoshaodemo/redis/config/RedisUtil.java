package com.sym.miaoshaodemo.redis.config;

import com.sym.miaoshaodemo.util.SerializerUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

/**
 * @author suyiming3333@gmail.com
 * @version V1.0
 * @Title: RedisUtil
 * @Package com.sym.miaoshaodemo.redis.service
 * @Description: TODO
 * @date 2019/9/25 14:40
 */

@Service
public class RedisUtil {

    @Autowired
    JedisPool jedisPool;

    /**
     * 获取缓存
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            String  str = jedis.get(realKey);
            T t =  SerializerUtil.jsonStringToBean(str, clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }


    /**
     * 删除key
     * @param prefix
     * @param key
     */
    public void del(String prefix, String key){
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            jedis.del(realKey);
        }finally {
            returnToPool(jedis);
        }
    }


    /**
     * 设置缓存
     * @param prefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(String prefix, String key ,int expireTime,  T value) {
        Jedis jedis = null;
        try {
            jedis =  jedisPool.getResource();
            String str = SerializerUtil.beanToJsonString(value);
            if(str == null || str.length() <= 0) {
                return false;
            }
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            int seconds =  expireTime;
            if(seconds <= 0) {
                jedis.set(realKey, str);
            }else {
                jedis.setex(realKey, seconds, str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * hash 设置多组键值
     * @param prefix
     * @param key
     * @param value
     * @return
     */
    public boolean hmset(String prefix,String key,Map<String, String> value){
        Jedis jedis = null;

        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            jedis.hmset(realKey, value);
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * hash 获取key所有键值
     * @param prefix
     * @param key
     * @return
     */
    public Map<String,String> hgetall(String prefix,String key){
        Jedis jedis = null;

        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            return jedis.hgetAll(realKey);
        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * list 往列表头部添加元素
     * @param prefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean lpush(String prefix,String key,T value){
        Jedis jedis = null;

        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            String str = SerializerUtil.beanToJsonString(value);
            jedis.lpush(realKey,str);
            return true;
        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * list 获取单个列表值
     * @param prefix
     * @param key
     * @param index
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T lindex(String prefix,String key,long index,Class<T> clazz){
        Jedis jedis = null;

        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            String str = jedis.lindex(realKey,index);
            T t =  SerializerUtil.jsonStringToBean(str, clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }

    }


    /**
     * list 获取列表
     * @param prefix
     * @param key
     * @param start
     * @param end
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> lrange(String prefix,String key,long start,long end,Class<T> clazz){
        Jedis jedis = null;

        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            List<String> list = jedis.lrange(realKey,start,end);
            String str = null;
            List<T> resultList = new ArrayList<>();
            for(int i=0;i<list.size();i++){
                str = list.get(i);
                T t =  SerializerUtil.jsonStringToBean(str, clazz);
                resultList.add(t);
            }
            return resultList;
        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * list 分页获取缓存列表
     * @param prefix
     * @param key
     * @param page
     * @param rows
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Map<Object,Object> lrangeByPage(String prefix,String key,long page,long rows,Class<T> clazz){
        Jedis jedis = null;
        Map<Object,Object> resultMap = new HashMap<>();
        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            long end = (page * rows)-1;
            long start = (page-1) * rows;
            List<String> list = jedis.lrange(realKey,start,end);
            long totalRecord = jedis.llen(realKey);
            long totalPage = (long) Math.ceil(totalRecord/rows);
            String str = null;
            List<T> resultList = new ArrayList<>();
            for(int i=0;i<list.size();i++){
                str = list.get(i);
                T t =  SerializerUtil.jsonStringToBean(str, clazz);
                resultList.add(t);
            }
            resultMap.put("data",resultList);
            resultMap.put("totalPage",totalPage);
            resultMap.put("currentPage",page);
            resultMap.put("totalRecord",totalRecord);
            resultMap.put("rows",rows);
            return resultMap;
        }finally {
            returnToPool(jedis);
        }

    }


    /**
     * sorted set 添加一个或多个键值对
     * @param prefix
     * @param key
     * @param value
     * @return
     */
    public boolean zadd(String prefix,String key,Map<String,Double> value){
        Jedis jedis = null;

        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            jedis.zadd(realKey,value);
            return true;
        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * sorted set 移除一个成员
     * @param prefix
     * @param key
     * @param member
     * @return
     */
    public boolean zrem(String prefix,String key,String member){
        Jedis jedis = null;

        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            jedis.zrem(realKey,member);
            return true;
        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * 按分低到高返回某成员的排名
     * @param prefix
     * @param key
     * @param member
     * @return
     */
    public long zrank(String prefix,String key,String member){
        Jedis jedis = null;

        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            return jedis.zrank(realKey,member);
        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * 按分数高到低返回某成员的排名
     * @param prefix
     * @param key
     * @param member
     * @return
     */
    public long zrevrank(String prefix,String key,String member){
        Jedis jedis = null;

        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            return jedis.zrevrank(realKey,member);

        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * sorted set 返回区间范围，分数由低到高的
     * @param prefix
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrange(String prefix, String key, long start, long end){
        Jedis jedis = null;

        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            return jedis.zrange(realKey,start,end);

        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * sorted set 返回区间范围，分数由高到低的
     * @param prefix
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrevrange(String prefix, String key, long start, long end){
        Jedis jedis = null;

        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            return jedis.zrevrange(realKey,start,end);

        }finally {
            returnToPool(jedis);
        }

    }

    /**
     * sorted set 返回有序集中指定分数区间内的成员，分数从高到低排序
     * @param prefix
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zrevrangeByScore(String prefix, String key, long start, long end){
        Jedis jedis = null;

        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey  = prefix;
            if(StringUtils.isNotBlank(key)){
                realKey  = prefix + "-" + key;
            }
            return jedis.zrevrangeByScore(realKey,start,end);

        }finally {
            returnToPool(jedis);
        }

    }


    /**
     * 关闭redis连接
     * @param jedis
     */
    private void returnToPool(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }

    public static void main(String[] args) {
        RedisUtil redisUtil = new RedisUtil();
        Map<String,String> studentMap = new HashMap<>();

        studentMap.put("id","1");
        studentMap.put("name","corn");
        studentMap.put("age","26");
        studentMap.put("set","0");
        studentMap.put("birth","1993");

        System.out.println(redisUtil.hmset("Student:","1",studentMap));
    }

}
