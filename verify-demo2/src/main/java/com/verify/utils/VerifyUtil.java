package com.verify.utils;

import com.google.gson.Gson;
import com.verify.dao.VerifyInfoDao;
import com.verify.dto.VerifyInfo;
import com.verify.service.impl.VerifyDBServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class VerifyUtil {

    public static final Gson gson = new Gson();

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private VerifyInfoDao verifyInfoDao;

    @Autowired
    public VerifyUtil(VerifyInfoDao verifyInfoDao){
        this.verifyInfoDao = verifyInfoDao;
    }

    /**
     * @param master 原数据源本时间段查询结果集
     * @param slave 迁移后数据源本时间段查询结果集
     * @param verifyInfo set了verifyTable，startDate，endDate的实例，通过修改masterEntity，slaveEntity来插入校验得到的异常数据
     */
    public void verifyResult(List<Map<String,Object>> master, List<Map<String,Object>> slave, VerifyInfo verifyInfo) {
        /*// TODO 将文件写入到指定目录（具体开发中有可能是将文件写入到云存储/或者指定目录通过 Nginx 进行 gzip 压缩和反向代理
        // 开发时，路径可以是目录中的路径，但是在生产时，是一个jar包在运行，所以我们要获取jar包运行的同级目录
        // 先获取idea中运行时候，classes目录下的路径
        File errorLog = new File(ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX).getPath() + tableName + "检验日志.txt");
        // 当生产环境时，jar包中的目录无法被获取，所以 upload不存在
        // 先 new File("").getAbsoluteFile() 获取 jar 包所在目录，然后拼接即可
        if (!errorLog.getParentFile().exists()) {
            errorLog = new File(new File("").getAbsoluteFile() + File.separator + tableName + "校验日志.txt");
        }
        // 表示追加，而不是覆盖
        BufferedWriter out = new BufferedWriter(new FileWriter(errorLog, true));*/

        if (master.size() == 0) {
            // log.info("原数据库无结果集，跳过此时间段对比");
            // out.write("[ "+tableName+" INFO ] —— 原数据库无结果集，跳过此时间段对比");
            return;
        }

        // 将list数据封装，便于查找
        Map<String, Map<String, Object>> masterEntitys = new HashMap<String, Map<String, Object>>();
        Map<String, Map<String, Object>> slaveEntitys = new HashMap<String, Map<String, Object>>();

        // 将List的数据封装到Map，方便后面对比时查找,有的表没有ID主键,而是CODE
        if (master.get(0).get("ID") == null) {
            for (int m = 0; m < master.size(); m++) {
                // 获取 list 中的 map，每个map 相当于一个entity，它们都有主键ID，然后我将主键ID作为key，map作为value，存入 Entitys 方便查找
                masterEntitys.put(master.get(m).get("CODE").toString(), master.get(m));
            }

            for (int s = 0; s < slave.size(); s++) {
                slaveEntitys.put(slave.get(s).get("CODE").toString(), slave.get(s));
            }
        }
        else {
            for (int m = 0; m < master.size(); m++) {
                // 获取 list 中的 map，每个map 相当于一个entity，它们都有主键ID，然后我将主键ID作为key，map作为value，存入 Entitys 方便查找
                masterEntitys.put(master.get(m).get("ID").toString(), master.get(m));
            }

            for (int s = 0; s < slave.size(); s++) {
                slaveEntitys.put(slave.get(s).get("ID").toString(), slave.get(s));
            }
        }
        // 清理原list，防止占用太多空间
        master.clear();
        slave.clear();
        // 初始化不匹配条数
        int unequal = 0;

        // 只要原数据库的数据迁移到新数据库即可，所以这里只要获得原数据库的keySet
        // 检验每行数据是否对应
        for (String masterId : masterEntitys.keySet()) {
            // 如果salve(新数据库)没有原数据库的这条记录，直接记录下来，然后跳过这次循环
            // slave 没有这条数据时，json字符串为迁移后的数据库缺少本条记录
            if (slaveEntitys.get(masterId) == null) {
                unequal++;
                verifyInfo.setMasterEntity(gson.toJson(masterEntitys.get(masterId)));
                verifyInfo.setSlaveEntity("迁移后的数据库缺少本条记录");
                verifyInfo.setCrtDate(new Timestamp(new Date().getTime()));
                verifyInfoDao.save(verifyInfo);
                /*log.error("数据库迁移信息出现异常，异常处 Master(原数据库) 为： "
                        + gson.toJson(masterEntitys.get(masterId))
                        + " —— 异常信息为： slave(新数据库) 没有该条数据");*/
                /*out.newLine();
                out.write("[ "+tableName+" ERROR ] —— 数据库迁移信息出现异常，异常处 Master(原数据库) 为： "
                        + gson.toJson(masterEntitys.get(masterId))
                        + " —— 异常信息为： slave(新数据库) 没有该条数据");*/
                continue;
            }
            Map<String, Object> thisMasterEntity =  masterEntitys.get(masterId);
            Map<String, Object> thisSlaveEntity = slaveEntitys.get(masterId);


            // 校验字段
            for (String masterKey : thisMasterEntity.keySet()) {
                // 这里有可能数据库中某个字段值为null，当发生这种情况时，就跳过当次循环
                if (thisMasterEntity.get(masterKey) == null) {
                    continue;
                }
                // 这时获取的是 新旧数据库 都拥有对应id的数据，然后对比其中每个字段是否相同，相同跳过，不相同记录
                if (thisMasterEntity.get(masterKey).toString().equals(thisSlaveEntity.get(masterKey).toString())) {
                    continue;
                }
                else {
                    unequal++;
                    verifyInfo.setMasterEntity(gson.toJson(thisMasterEntity));
                    verifyInfo.setSlaveEntity(gson.toJson(thisSlaveEntity));
                    verifyInfo.setCrtDate(new Timestamp(new Date().getTime()));
                    verifyInfoDao.save(verifyInfo);
                    // 只要有一个字段不匹配,就记录于数据库中,然后跳出这个列对比循环
                    break;
                    /*log.error("数据库迁移信息出现异常，异常处 Master 为： "
                            + gson.toJson(thisMasterEntity)
                            + " —— 异常信息为： 字段内容不匹配"
                            + " —— 异常处 Salve 为： " + gson.toJson(thisSlaveEntity));*/
                    /*out.newLine();
                    out.write("[ "+tableName+" ERROR ] —— 数据库迁移信息出现异常，异常处 Master 为： "
                            + gson.toJson(thisMasterEntity)
                            + " —— 异常信息为： 字段内容不匹配"
                            + " —— 异常处 Salve 为： " + gson.toJson(thisSlaveEntity));*/
                }
            }

        }

        // 记录不匹配条数
        VerifyDBServiceImpl.unequals.put(verifyInfo.getVerifyTable() + verifyInfo.getStartDate() + verifyInfo.getEndDate(), unequal);

        //out.flush();
        //out.close();
    }

}