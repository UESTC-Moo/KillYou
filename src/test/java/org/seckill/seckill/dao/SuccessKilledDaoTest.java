package org.seckill.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.seckill.entity.SuccessKilled;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void testeInsertSuccessKilled() throws Exception{
        int insertCount = successKilledDao.insertSuccessKilled(1000L,15002894830L);
        System.out.println(insertCount);
    }

    @Test
    public void testQueryByIdWithSeckill(){
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000L,15002894830L);
        System.out.println(successKilled.toString());
    }

}