package cn.luxinhuo.redislearn.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
@Slf4j
public class JedisConfig {

    @Value("${spring.redis.host:localhost}")
    private String host;

    @Value("${spring.redis.port:0}")
    private int port;

    @Value("${spring.redis.password:null}")
    private String password;

    @Value("${spring.redis.timeout:0}")
    private int timeout;

    @Value("${spring.redis.database:0}")
    private int database;

    @Bean
    public JedisPool getJedisPool(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100);
        poolConfig.setMaxWaitMillis(1000);

        JedisPool jedisPool = new JedisPool(poolConfig,host, port,timeout,password,database);
        log.info("jedis连接池配置成功(host:{},port:{},timeout:{},password:{},database:{}",
                host,port,timeout,password,database);

        log.info("{}",host);
        return jedisPool;
    }

    public static JedisPool getJedis(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 6379, 100, "123", 0);
        return jedisPool;
    }
}
