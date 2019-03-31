package org.seckill.seckill.mq;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.seckill.config.RabbitMQConfig;
import org.seckill.seckill.dao.RedisDao;
import org.seckill.seckill.entity.Seckill;
import org.seckill.seckill.entity.SeckillMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
public class MQSender {
    @Autowired
    private RedisDao redisDao;

    @Autowired
    AmqpTemplate amqpTemplate;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RuntimeSchema<SeckillMessage> schema = RuntimeSchema.createFrom(SeckillMessage.class);

    public void send(SeckillMessage message){
        byte[] bytes = ProtostuffIOUtil.toByteArray(message, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));

        amqpTemplate.convertAndSend(RabbitMQConfig.QUEUE,bytes);

    }
}
