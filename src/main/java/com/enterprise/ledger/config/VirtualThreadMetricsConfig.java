package com.enterprise.ledger.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;
import java.lang.management.ManagementFactory;

/**
 * Hardened infrastructure configuration utilizing direct platform interface casting.
 * Provides stable, type-safe metrics tracking for Java 21 Virtual Threads.
 */
@Configuration
public class VirtualThreadMetricsConfig {

    public VirtualThreadMetricsConfig(MeterRegistry registry) {
        java.lang.management.ThreadMXBean baseBean = ManagementFactory.getThreadMXBean();
        
        // Check if the underlying bean supports modern extended HotSpot monitoring functions
        if (baseBean instanceof com.sun.management.ThreadMXBean) {
            com.sun.management.ThreadMXBean extendedMXBean = (com.sun.management.ThreadMXBean) baseBean;
            
            Gauge.builder("jvm.threads.virtual.vthreads", extendedMXBean, bean -> {
                try {
                    return (double) bean.getVirtualThreadCount();
                } catch (Exception e) {
                    return 0.0;
                }
            })
            .description("The current number of active user-space virtual threads running in the JVM heap")
            .baseUnit("threads")
            .register(registry);
        }
    }
}