package com.qflu.verify.utils;

import com.google.gson.Gson;
import org.springframework.util.ResourceUtils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class VerifyUtil {

    public static final Gson gson = new Gson();

    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void verifyResult(List<Map<String,Object>> master, List<Map<String,Object>> slave, String tableName) throws IOException {
        // TODO 将文件写入到指定目录（具体开发中有可能是将文件写入到云存储/或者指定目录通过 Nginx 进行 gzip 压缩和反向代理
        // 开发时，路径可以是目录中的路径，但是在生产时，是一个jar包在运行，所以我们要获取jar包运行的同级目录
        // 先获取idea中运行时候，classes目录下的路径
        File errorLog = new File(ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX).getPath() + tableName + "检验日志.txt");
        // 当生产环境时，jar包中的目录无法被获取，所以 upload不存在
        // 先 new File("").getAbsoluteFile() 获取 jar 包所在目录，然后拼接即可
        if (!errorLog.getParentFile().exists()) {
            errorLog = new File(new File("").getAbsoluteFile() + File.separator + tableName + "校验日志.txt");
        }
        // 表示追加，而不是覆盖
        BufferedWriter out = new BufferedWriter(new FileWriter(errorLog, true));

        // 如果master比较大，返回master的大小，否则返回slave的。
        int size = master.size()>slave.size()?master.size():slave.size();

        // 代表每次循环转为的json字符串
        String thisMaster = "";
        String thisSlave = "";

        for (int i = 0; i < size; i++) {
            // 从master、slave中遍历得到所有的map，直接转为json字符串然后equals进行比较，如果不对则记录下来
            thisMaster = gson.toJson(master.get(i));
            thisSlave = gson.toJson(slave.get(i));
            if (thisMaster.equals(thisSlave)) {
                continue;
            }
            else {
                out.write("[ "+tableName+" ERROR ] —— 数据库迁移信息出现异常，异常处 Master 为： " + thisMaster
                        + " —— 异常处 Salve 为： " + thisSlave + System.lineSeparator());
            }
        }
        out.flush();
        out.close();
    }

}
