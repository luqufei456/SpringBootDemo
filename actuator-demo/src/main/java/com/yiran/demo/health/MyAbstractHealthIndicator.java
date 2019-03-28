package com.yiran.demo.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

/**
 * <p>自定义健康端点</p>
 * <p>功能更加强大一点，DataSourceHealthIndicator / RedisHealthIndicator 都是这种写法</p>
 */
@Component("my2")
public class MyAbstractHealthIndicator extends AbstractHealthIndicator {
    private static final String VERSION = "v1.0.0";

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        int code = check();
        if (code != 0) {
            builder.down().withDetail("code", code).withDetail("version", VERSION).build();
        }
        builder.up().withDetail("code", code)
                .withDetail("version", VERSION).build();
    }

    private int check() {
        return 0;
    }
}
