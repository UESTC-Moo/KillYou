package org.seckill.seckill.dao;

import org.apache.ibatis.annotations.Mapper;
import org.seckill.seckill.entity.SuccessKilled;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface SuccessKilledDao {

    int insertSuccessKilled(long seckillId, long userPhone);

    SuccessKilled queryByIdWithSeckill(long seckillId,long userPhone);

}
