package org.seckill.seckill.service;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.seckill.dto.Exposer;
import org.seckill.seckill.dto.SeckillExecution;
import org.seckill.seckill.entity.Seckill;
import org.seckill.seckill.entity.SeckillMessage;
import org.seckill.seckill.exception.RepeatKillException;
import org.seckill.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;
    @Test
    public void testGetSeckillList(){
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void testGetSeckillById(){
        long id = 1000;
        Seckill seckill = seckillService.getSeckillById(id);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void testExportSeckillUrl(){
        long id = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}",exposer);
        //exposer=Exposer{exposed=true, md5='eabeca733ac93678c9696329efea8b58', seckillId=1000, now=0, start=0, end=0}
    }

    @Test
    public void testExecuteSeckill(){
        long id = 1000;
        long phone = 132541341516L;
        String md5 = "eabeca733ac93678c9696329efea8b58";
        try{
            SeckillExecution seckillExecution = seckillService.executeSeckill(id,phone,md5);
            logger.info("result={}",seckillExecution);
        }catch (RepeatKillException e){
            logger.error(e.getMessage(),e);
        }catch (SeckillCloseException e){
            logger.error(e.getMessage(),e);
        }
    }

    @Test
    public void testSeckillLogic(){
        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if(exposer.isExposed()){
            long phone = 132541341516L;
            String md5 = exposer.getMd5();
            try{
                SeckillExecution seckillExecution = seckillService.executeSeckill(id,phone,md5);
                logger.info("result={}",seckillExecution);
            }catch (RepeatKillException e){
                logger.error(e.getMessage(),e);
            }catch (SeckillCloseException e){
                logger.error(e.getMessage(),e);
            }
        }else{
            logger.warn("exposer={}",exposer);
        }
    }

    @Test
    public void testExecuteSeckillProcedure(){
        long id = 1002;
        long phone = 132541341516L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if(exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(id,phone,md5);
            logger.info(execution.getStateInfo());
        }

    }

    @Test
    public void testExecuteSeckillMq(){
        long id = 1002;
        long phone = 132541341467L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if(exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillMq(id,phone,md5);
            logger.info(execution.getStateInfo());
        }

    }

    @Test
    public void testProtoStuff(){
        RuntimeSchema<SeckillMessage> schema = RuntimeSchema.createFrom(SeckillMessage.class);
        SeckillMessage seckillMessage = new SeckillMessage(1003L,15002894830L,"dasfdasfaf");
        byte[] result = ProtostuffIOUtil.toByteArray(seckillMessage,schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        String msg = new String(result);


        SeckillMessage message = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(msg.getBytes(),message,schema);
        logger.info(message.toString());



    }
}