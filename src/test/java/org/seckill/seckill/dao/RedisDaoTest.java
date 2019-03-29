package org.seckill.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisDaoTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private long id = 1000;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testGetSeckill() {
        Seckill seckill = redisDao.getSeckill(id);
        logger.info(seckill.toString());
    }

    @Test
    public void testPutSeckill() {
        Seckill seckill = seckillDao.queryById(id);
        String result = redisDao.putSeckill(seckill);
        logger.info(result);
    }
}