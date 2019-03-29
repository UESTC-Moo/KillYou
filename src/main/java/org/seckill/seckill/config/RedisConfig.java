package org.seckill.seckill.config;

import org.seckill.seckill.dao.RedisDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    @Bean
    public RedisDao redisDao(){
        return new RedisDao("192.168.188.128",6379);
    }
}
