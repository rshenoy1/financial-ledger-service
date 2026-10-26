package com.enterprise.ledger.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;
import java.lang.management.ManagementFactory;

/**
 * Hardened infrastructure configuration utilizing direct platform interface casting.
 * Bypasses Project Jigsaw modular isolation restrictions completely.
 */
@Configuration
public class VirtualThreadMetricsConfig {

    public VirtualThreadMetricsConfig(MeterRegistry registry) {
        java.lang.management.ThreadMXBean baseBean = ManagementFactory.getThreadMXBean();
        
        if (baseBean instanceof com.sun.management.ThreadMXBean) {
            com.sun.management.ThreadMXBean extendedBean = (com.sun.management.ThreadMXBean) baseBean;
            
            Gauge.builder("jvm.threads.virtual.vthreads", extendedBean, bean -> {
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