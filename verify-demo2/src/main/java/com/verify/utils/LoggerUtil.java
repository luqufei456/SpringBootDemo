package com.verify.utils;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.springframework.util.ResourceUtils;

import java.io.File;


public class LoggerUtil {

    public static Logger getLoggerByName(String name) {
        // 生成新的Logger
        // 如果已經有了一個Logger實例返回現有的
        Logger logger = Logger.getLogger(name);
        // 清空Appender。特別是不想使用現存實例時一定要初期化
        logger.removeAllAppenders();
        // 設定Logger級別。
        logger.setLevel(Level.DEBUG);
        // 設定是否繼承父Logger。
        // 默認為true。繼承root輸出。
        // 設定false後將不輸出root。
        logger.setAdditivity(true);
        // 生成新的Appender
        FileAppender appender = new RollingFileAppender();
        PatternLayout layout = new PatternLayout();
        // log的输出形式
        String conversionPattern = "[%d] %p %t %c - %m%n";
        layout.setConversionPattern(conversionPattern);
        appender.setLayout(layout);
        // log输出路径
        appender.setFile(new File("").getAbsoluteFile() + File.separator + "verify" + File.separator + name + ".log");
        // log的文字码
        appender.setEncoding("UTF-8");
        // true:在已存在log文件后面追加 false:新log覆盖以前的log
        appender.setAppend(true);
        // 适用当前配置
        appender.activateOptions();
        // 将新的Appender加到Logger中
        logger.addAppender(appender);
        return logger;
    }
}