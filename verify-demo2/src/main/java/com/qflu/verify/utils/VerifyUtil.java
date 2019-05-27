package com.qflu.verify.utils;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.springframework.util.ResourceUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VerifyUtil {

    public static final Gson gson = new Gson();

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void verifyResult(List<Map<String,Object>> master, List<Map<String,Object>> slave, String tableName, Logger log) throws IOException {
        // TODO 将文件写入到指定目录（具体开发中有可能是将文件写入到云存储/或者指定目录通过 Nginx 进行 gzip 压缩和反向代理
        // 开发时，路径可以是目录中的路径，但是在生产时，是一个jar包在运行，所以我们要获取jar包运行的同级目录
        // 先获取idea中运行时候，classes目录下的路径
        /*File errorLog = new File(ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX).getPath() + tableName + "检验日志.txt");
        // 当生产环境时，jar包中的目录无法被获取，所以 upload不存在
        // 先 new File("").getAbsoluteFile() 获取 jar 包所在目录，然后拼接即可
        if (!errorLog.getParentFile().exists()) {
            errorLog = new File(new File("").getAbsoluteFile() + File.separator + tableName + "校验日志.txt");
        }
        // 表示追加，而不是覆盖
        BufferedWriter out = new BufferedWriter(new FileWriter(errorLog, true));*/

        if (master.size() == 0) {
            log.info("{} —— 原数据库无结果集，跳过此时间段对比", tableName);
            // out.write("[ "+tableName+" INFO ] —— 原数据库无结果集，跳过此时间段对比");
            return;
        }

        // 将list数据封装，便于查找
        Map<String, Map<String, Object>> masterEntitys = new HashMap<>();
        Map<String, Map<String, Object>> slaveEntitys = new HashMap<>();

        // 将List的数据封装到Map，方便后面对比时查找
        for (int m = 0; m < master.size(); m++) {
            // 获取 list 中的 map，每个map 相当于一个entity，它们都有主键ID，然后我将主键ID作为key，map作为value，存入 Entitys 方便查找
            masterEntitys.put(master.get(m).get("ID").toString(), master.get(m));
        }

        for (int s = 0; s < slave.size(); s++) {
            slaveEntitys.put(slave.get(s).get("ID").toString(), slave.get(s));
        }

        // 只要原数据库的数据迁移到新数据库即可，所以这里只要获得原数据库的keySet
        for (String masterId : masterEntitys.keySet()) {
            // 如果salve(新数据库)没有原数据库的这条记录，直接记录下来，然后跳过这次循环
            if (slaveEntitys.get(masterId) == null) {
                log.error("{} —— 数据库迁移信息出现异常，异常处 Master(原数据库) 为：{} —— 异常信息为： slave(新数据库) 没有该条数据",
                        tableName, gson.toJson(masterEntitys.get(masterId)));
                /*out.write("[ "+tableName+" ERROR ] —— 数据库迁移信息出现异常，异常处 Master(原数据库) 为： "
                        + gson.toJson(masterEntitys.get(masterId))
                        + " —— 异常信息为： slave(新数据库) 没有该条数据" + System.lineSeparator());*/
                continue;
            }
            Map<String, Object> thisMasterEntity =  masterEntitys.get(masterId);
            Map<String, Object> thisSlaveEntity = slaveEntitys.get(masterId);
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
                    log.error("{} —— 数据库迁移信息出现异常，异常处 Master 为： {}"
                            + " —— 异常信息为： 字段内容不匹配"
                            + " —— 异常处 Salve 为： {} ", tableName, gson.toJson(thisMasterEntity), gson.toJson(thisSlaveEntity));
                    /*out.write("[ "+tableName+" ERROR ] —— 数据库迁移信息出现异常，异常处 Master 为： "
                            + gson.toJson(thisMasterEntity)
                            + " —— 异常信息为： 字段内容不匹配"
                            + " —— 异常处 Salve 为： " + gson.toJson(thisSlaveEntity) + System.lineSeparator());*/
                }
            }

        }

        /*out.flush();
        out.close();*/
    }

}
