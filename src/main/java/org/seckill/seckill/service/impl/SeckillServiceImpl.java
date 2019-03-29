package org.seckill.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.seckill.dao.RedisDao;
import org.seckill.seckill.dao.SeckillDao;
import org.seckill.seckill.dao.SuccessKilledDao;
import org.seckill.seckill.dto.Exposer;
import org.seckill.seckill.dto.SeckillExecution;
import org.seckill.seckill.entity.Seckill;
import org.seckill.seckill.entity.SuccessKilled;
import org.seckill.seckill.enums.SeckillStatEnum;
import org.seckill.seckill.exception.RepeatKillException;
import org.seckill.seckill.exception.SeckillCloseException;
import org.seckill.seckill.exception.SeckillException;
import org.seckill.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    private final String slat = "askfdklnolibgva23(*1sdf2ewqd3$23#@@Eefsdf13";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getSeckillById(long seckill) {
        return seckillDao.queryById(seckill);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //Redis缓存优化

        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            seckill = getSeckillById(seckillId);
            if (seckill == null)
                return new Exposer(false, seckillId);
            else
                redisDao.putSeckill(seckill);
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date now = new Date();
        if (now.getTime() < startTime.getTime() || now.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, now.getTime(), startTime.getTime(), endTime.getTime());
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    /**
     * 事务方法执行时间尽可能短，不要穿插网络操作，可以放到事务外部
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        try {

            Date now = new Date();


//            int updateCount = seckillDao.reduceNumber(seckillId, now);
//            if (updateCount <= 0) {
//                throw new SeckillCloseException("seckill is closed");
//            } else {
//                int insetCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
//                if (insetCount <= 0) {
//                    throw new RepeatKillException("seckill repeated");
//                } else {
//                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
//                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
//                }
//            }

            int insetCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insetCount <= 0) {
                throw new RepeatKillException("seckill repeated");
            } else {
                int updateCount = seckillDao.reduceNumber(seckillId, now);
                if (updateCount <= 0) {
                    throw new SeckillCloseException("seckill is closed");
                }else{
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }

            }

        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }
    }

    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            return new SeckillExecution(seckillId, SeckillStatEnum.DATA_REWRITE);
        }
        Date now = new Date();
        Map<String,Object> map = new HashMap<>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",now);
        map.put("result",null);
        try{
            seckillDao.killByProcedure(map);
            int result = MapUtils.getInteger(map,"result",-2);
            if(result == 1){
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
            }else{
                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
        }
    }
}
