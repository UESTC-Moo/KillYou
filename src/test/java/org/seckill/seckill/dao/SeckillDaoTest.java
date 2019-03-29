package org.seckill.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SeckillDaoTest {
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void testReduceNumber() throws Exception{
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1001L,killTime);
        System.out.println("updateCount: " + updateCount);
    }

    @Test
    public void testQueryAll() throws Exception{
        long id = 1000;
        List<Seckill> seckills = seckillDao.queryAll(0,100);
        seckills.stream().forEach(list-> System.out.println(list.toString()));

    }
}