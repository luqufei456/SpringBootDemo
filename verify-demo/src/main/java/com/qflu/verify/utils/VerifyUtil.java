package com.qflu.verify.utils;

import com.qflu.verify.entity.User;
import org.springframework.util.ResourceUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class VerifyUtil {

    public void verifyResult(List<User> users1, List<User> users2, String rule) throws IOException {
        // TODO 将文件写入到指定目录（具体开发中有可能是将文件写入到云存储/或者指定目录通过 Nginx 进行 gzip 压缩和反向代理
        // 开发时，路径可以是目录中的路径，但是在生产时，是一个jar包在运行，所以我们要获取jar包运行的同级目录
        // 先获取idea中运行时候，classes目录下的路径
        File errorLog = new File(ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX).getPath() + rule + ".txt");
        // 当生产环境时，jar包中的目录无法被获取，所以 upload不存在
        // 先 new File("").getAbsoluteFile() 获取 jar 包所在目录，然后拼接即可
        if (!errorLog.getParentFile().exists()) {
            errorLog = new File(new File("").getAbsoluteFile() + File.separator + rule + ".txt");
        }
        BufferedWriter out = new BufferedWriter(new FileWriter(errorLog));

        int size = users1.size()>users2.size()?users1.size():users2.size();
        users1 = userSortById(users1);
        users2 = userSortById(users2);
        for (int i = 0; i < size; i++) {
            if (users1.get(i).equals(users2.get(i))) {
                continue;
            }
            else {
                out.write("[ "+rule+" ] —— By 主键为 " + users1.get(i).getId()
                        + " 处产生对比异常 [ " + users1.get(i) + " ] != [ " + users2.get(i) + " ]"
                        + System.lineSeparator());
            }
        }
        out.flush();
        out.close();
    }

    private List<User> userSortById(List<User> users) {
        Collections.sort(users, (o1, o2) -> {
            User user1 = o1;
            User user2 = o2;
            // 返回负数 小->大 升序，返回正数 大->小 降序
            return (int)(user1.getId() - user2.getId());
        });
        return users;
    }
}
