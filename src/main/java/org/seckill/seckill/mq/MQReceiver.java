package org.seckill.seckill.mq;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.apache.commons.collections.MapUtils;
import org.seckill.seckill.config.RabbitMQConfig;
import org.seckill.seckill.dao.SeckillDao;
import org.seckill.seckill.dao.SuccessKilledDao;
import org.seckill.seckill.dto.SeckillExecution;
import org.seckill.seckill.entity.Seckill;
import org.seckill.seckill.entity.SeckillMessage;
import org.seckill.seckill.entity.SuccessKilled;
import org.seckill.seckill.enums.SeckillStatEnum;
import org.seckill.seckill.exception.RepeatKillException;
import org.seckill.seckill.exception.SeckillCloseException;
import org.seckill.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MQReceiver {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    private RuntimeSchema<SeckillMessage> schema = RuntimeSchema.createFrom(SeckillMessage.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiver(byte[] message) throws SeckillException, RepeatKillException, SeckillCloseException{
        logger.info("recevier " + message.toString());

        if (message != null) {
            SeckillMessage seckillMessage = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(message, seckillMessage, schema);

            Date now = new Date();
            Map<String, Object> map = new HashMap<>();
            map.put("seckillId", seckillMessage.getSeckillId());
            map.put("phone", seckillMessage.getUserPhone());
            map.put("killTime", now);
            map.put("result", null);

            SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillMessage.getSeckillId(), seckillMessage.getUserPhone());
            if (successKilled != null) {
                throw new RepeatKillException(SeckillStatEnum.REPEAT_KILL.getStateInfo());
            }
            try {
                seckillDao.killByProcedure(map);
                int result = MapUtils.getInteger(map, "result", -2);
                if (result == 1) {
                    return;
                } else {
                    throw new SeckillException(SeckillStatEnum.stateOf(result).getStateInfo());
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new SeckillCloseException(SeckillStatEnum.INNER_ERROR.getStateInfo());
            }

        }else{
            logger.error("messgae is null");
        }
    }
}
