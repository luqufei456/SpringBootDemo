package com.qflu.verify;

import com.google.gson.Gson;
import com.qflu.verify.utils.VerifyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;

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

        String table = "t_user";
        String sql = "select * from " + table + " where username = ? ";
        String master = gson.toJson(masterJdbcTemplate.queryForList(sql, "u1"));
        String slave = gson.toJson(slaveJdbcTemplate.queryForList(
                "select * from t_user where username = ?","u1"));
        System.out.println(master);
        System.out.println(slave);
        System.out.println(master.equals(slave));
    }

    @Test
    public void test2() throws ParseException {
        String startDate = "2018-05-15 00:00:00";
        System.out.println(VerifyUtil.sdf.format(VerifyUtil.sdf.parse(startDate)));

    }

}
