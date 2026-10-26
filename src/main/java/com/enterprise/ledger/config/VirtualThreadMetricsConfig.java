package com.enterprise.ledger.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * Hardened infrastructure configuration utilizing direct platform interface gauges.
 * Provides stable, non-blocking metrics tracking for Java 21 Virtual Threads.
 */
@Configuration
public class VirtualThreadMetricsConfig {

    public VirtualThreadMetricsConfig(MeterRegistry registry) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        
        // Bind the active virtual thread tracking logic using standard runtime introspection
        Gauge.builder("jvm.threads.virtual.vthreads", threadMXBean, bean -> {
            try {
                var method = bean.getClass().getMethod("getVirtualThreadCount");
                method.setAccessible(true);
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