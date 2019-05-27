package com.verify.dao;

import com.verify.dto.VerifyInfo;

/**
 * 添加校验详情结果
 */
public interface VerifyInfoDao {

    /**
     * 添加校验详情结果,其中字段为,表名,起始时间段,终止时间段,原数据源实体json字符串,迁移后数据源json字符串,
     * 配置中心起始时间,终止时间,用于鉴别是否校验,防止定时器重复校验.crtDate，创建时间
     * @param verifyInfo 实体
     */
    void save(VerifyInfo verifyInfo);
}
