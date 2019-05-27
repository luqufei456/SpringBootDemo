package com.verify.dao;

import com.verify.dto.VerifyResult;


public interface VerifyResultDao {

    /**
     * 记录每个时间间隔,对比表名,起始时间,终止时间,对比条数,不同条数，创建时间
     * @param verifyResult 实体
     */
    void save(VerifyResult verifyResult);

    /**
     * 根据表名，配置中心起始时间，终止时间，查询这个配置的时间段是否被校验过，若校验过则不再进行校验
     * @param verifyResult 实体
     * @return 查询结果
     */
    Integer findByVerifyResult(VerifyResult verifyResult);
}
