package com.enterprise.ledger.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Method;

/**
 * Custom infrastructure telemetry binder hooking directly into JVM reflection counters.
 * Dynamically registers active Java 21 Virtual Thread metrics into the Prometheus registry.
 */
@Configuration
public class VirtualThreadMetricsConfig {

    public VirtualThreadMetricsConfig(MeterRegistry registry) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        
        // Use reflection to safely capture Java 21 virtual thread counts across Alpine platforms
        Gauge.builder("jvm.threads.virtual.vthreads", threadMXBean, bean -> {
            try {
                Method method = bean.getClass().getMethod("getVirtualThreadCount");
                return ((Number) method.invoke(bean)).doubleValue();
            } catch (Exception e) {
                return 0.0;
            }
        })
        .description("The current number of active user-space virtual threads running in the JVM heap")
        .baseUnit("threads")
        .register(registry);
    }
}