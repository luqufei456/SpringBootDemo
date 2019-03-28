package com.yiran.demo.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * <p>自定义健康端点</p>
 */
@Component("my1") // 把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>
public class MyHealthIndicator implements HealthIndicator {
    private static final String VERSION = "V1.0.0";

    @Override
    public Health health() {
        int code = check();
        if (code != 0) {
            return Health.down().withDetail("code", code).withDetail("version", VERSION).build();
        }
        return Health.up().withDetail("code", code)
                .withDetail("version", VERSION).up().build();
    }

    private int check(){
        return 1;
    }
}
