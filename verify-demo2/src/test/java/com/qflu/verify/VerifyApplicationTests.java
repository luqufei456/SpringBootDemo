package com.qflu.verify;

import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VerifyApplicationTests {

    @Autowired
    @Qualifier("masterJdbcTemplate")
    protected JdbcTemplate masterJdbcTemplate;

    @Autowired
    @Qualifier("slaveJdbcTemplate")
    protected JdbcTemplate slaveJdbcTemplate;

    @Test
    public void test1() {
        Gson gson = new Gson();

        String master = gson.toJson(masterJdbcTemplate.queryForList(
                "select * from t_user where username = ?","u1"));
        String slave = gson.toJson(slaveJdbcTemplate.queryForList(
                "select * from t_user where username = ?","u1"));
        System.out.println(master.equals(slave));
    }

}
