package cn.luxinhuo.redislearn.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.*;

import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@RestController
public class Jedis2 {

//    @Autowired
    private JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379, 100, "123", 0);

    private Jedis jedis = jedisPool.getResource();

    //redis的pipeline功能使用,批量像redis中存储数据
    @GetMapping("/pipeline")
    public void testPipeline(){
        Pipeline pipelined = jedis.pipelined();
        for (int i = 0; i < 10000; i++) {
            pipelined.hset("myMap","field"+i,"value"+i);
        }
        pipelined.syncAndReturnAll();
    }

    @Test
    public void  testJedisTool(){
        byte[] key1 = "byte".getBytes();
        byte[] value1 = "byteTest".getBytes();
        String setResult = jedis.set(key1, value1);
        System.out.println(setResult);
    }

    @Test
    public void testBitMap(){
        jedis.del("mybit");
        Boolean mybitSet = jedis.setbit("mybit", 127, true);
        String mybitGet = jedis.get("mybit");
        

        System.out.println(Arrays.toString(mybitGet.getBytes()));

    }

    @Test
    public void testHyperLoglog(){
        jedis.del("perlog");
        Pipeline pipelined = jedis.pipelined();
        for (int i = 0; i < 200; i++) {
            pipelined.pfadd("perlog","value"+i);
        }
        pipelined.sync();

        long perlog = jedis.pfcount("perlog");
        System.out.println(perlog);
    }
    
    @Test
    public void testHash(){
        Long hset = jedis.hset("myMap", "field", "value1");

        Pipeline pipelined = jedis.pipelined();
        Response<Map<String, String>> myMap = pipelined.hgetAll("myMap");
    }
}
