package com.qflu.verify;

import com.qflu.verify.entity.User;
import com.qflu.verify.mapper.mapperMaster.UserMasterMapper;
import com.qflu.verify.mapper.mapperSlave.UserSlaveMapper;
import com.qflu.verify.utils.VerifyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VerifyApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(VerifyApplicationTests.class);

    @Autowired
    private UserMasterMapper userMasterMapper;

    @Autowired
    private UserSlaveMapper userSlaveMapper;

    private VerifyUtil verifyUtil = new VerifyUtil();

    @Test
    public void masterInsert() {
        /*final int row1 = userMasterMapper.insert(new User("u1", "p1"));
        log.info("[添加结果] - [{}]", row1);
        final int row2 = userMasterMapper.insert(new User("u2", "p2"));
        log.info("[添加结果] - [{}]", row2);
        final int row3 = userMasterMapper.insert(new User("u1", "p3"));
        log.info("[添加结果] - [{}]", row3);*/
        final List<User> u1 = userMasterMapper.findByUsername("u1");
        // log.info("[根据用户名查询-Master] - [{}]", u1);
        final List<User> u2 = userSlaveMapper.findByUsername("u1");
        // log.info("[根据用户名查询-Slave] - [{}]", u2);
        try {
            verifyUtil.verifyResult(u1, u2, "测试");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
