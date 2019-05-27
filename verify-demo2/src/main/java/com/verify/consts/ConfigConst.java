package com.verify.consts;

import com.google.gson.Gson;
import com.verify.dto.DataSourceDto;
import com.verify.dto.TableDto;
import com.verify.utils.VerifyUtil;

public class ConfigConst {
    public static void main(String[] args) {
        dataSourceDto = new DataSourceDto();
        dataSourceDto.setMasterDriver("oracle.jdbc.driver.OracleDriver");
        dataSourceDto.setMasterUrl("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=172.25.5.217)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=developmu)(SERVER=DEDICATED)))");
        dataSourceDto.setMasterUsername("dST1srgkddw=");
        dataSourceDto.setMasterPassword("3lJOph6EV8/+GovOshzl8g==");

        dataSourceDto.setSlaveDriver("oracle.jdbc.driver.OracleDriver");
        dataSourceDto.setSlaveUrl("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=172.25.5.217)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=developmu)(SERVER=DEDICATED)))");
        dataSourceDto.setSlaveUsername("dST1srgkddw=");
        dataSourceDto.setSlavePassword("3lJOph6EV8/+GovOshzl8g==");

        dataSourceDto.setOnOff(true);
        dataSourceDto.setUpdate(true);
        System.out.println(VerifyUtil.gson.toJson(dataSourceDto));

        TableDto tableDto = new TableDto();
        tableDto.setTableName("API_PERMIT");
        tableDto.setCrtName("CRT_DT");
        tableDto.setStartDate("2019-04-01");
        tableDto.setFinalEndDate("2019-05-09");
        tableDto.setInterval(5);
        tableDto.setOnOff(true);
        System.out.println(VerifyUtil.gson.toJson(tableDto));
    }

    public static DataSourceDto dataSourceDto;

    public static TableDto table1;

    public static TableDto table2;

    public static TableDto table3;

    public static TableDto table4;

    public static TableDto table5;

}
