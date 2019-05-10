package cn.luxinhuo.redislearn.controller;

import cn.luxinhuo.redislearn.aop.AopLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.*;

import java.util.Objects;

@Slf4j
@RestController
public class Redis1 {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private JedisPool jedisPool;

    private JedisCluster jedisCluster = new JedisCluster(new HostAndPort("127.0.0.1",7000));

    @AopLog(description = "我是描述信息")
    @RequestMapping("/getRedisTemplateValue")
    public String getRedisValue(@RequestParam("key") String key){
        ValueOperations<String, Object> stringOps = redisTemplate.opsForValue();
        HashOperations<String, Object, Object> hashOps = redisTemplate.opsForHash();
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        SetOperations<String, Object> setOps = redisTemplate.opsForSet();

        return stringOps.get(key).toString();
    }

    @AopLog(description = "测试一下信息")
    @GetMapping("/getJedisPoolValue")
    public String getJedisPoolValue(@RequestParam("key") String key){
        jedisPool.getResource();

        Transaction multi = jedisPool.getResource().multi();
        multi.exec();
        return jedisPool.getResource().get(key);
    }

    @AopLog(description = "测试2下信息")
    public String getJedisPoolValue(@RequestParam("key") int key){
        jedisPool.getResource();

        return jedisPool.getResource().get(key+"");
        jedisCluster.re

    }
}
