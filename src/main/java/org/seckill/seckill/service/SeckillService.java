package org.seckill.seckill.service;

import org.seckill.seckill.dto.Exposer;
import org.seckill.seckill.dto.SeckillExecution;
import org.seckill.seckill.entity.Seckill;
import org.seckill.seckill.exception.RepeatKillException;
import org.seckill.seckill.exception.SeckillCloseException;
import org.seckill.seckill.exception.SeckillException;

import java.util.List;

public interface SeckillService {

    List<Seckill> getSeckillList();

    Seckill getSeckillById(long seckill);

    /**
     * 秒杀开始输出秒杀接口地址
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;

    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
}
