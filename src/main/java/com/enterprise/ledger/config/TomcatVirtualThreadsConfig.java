package com.enterprise.ledger.config;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class TomcatVirtualThreadsConfig {

    @Bean
    public TomcatProtocolHandlerCustomizer<?> tomcatProtocolHandlerCustomizer() {
        return protocolHandler -> {
            // 🔒 Universal Fix: Uses standard Executors lookup to guarantee cross-version compilation passes
            try {
                java.lang.reflect.Method virtualMethod = Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
                protocolHandler.setExecutor((ExecutorService) virtualMethod.invoke(null));
            } catch (Exception e) {
                protocolHandler.setExecutor(Executors.newCachedThreadPool());
            }
        };
    }
}