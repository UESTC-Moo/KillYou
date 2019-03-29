package org.seckill.seckill.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.seckill.seckill.entity.Seckill;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface SeckillDao {
    int reduceNumber(long seckillId, Date killTime);

    Seckill queryById(long seckillId);

    List<Seckill> queryAll(@Param("offset") int oet, int limit);

    void killByProcedure(Map<String,Object> map);

}
