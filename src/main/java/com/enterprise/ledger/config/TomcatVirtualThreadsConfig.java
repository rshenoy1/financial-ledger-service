package com.enterprise.ledger.config;

import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.Executors;

@Configuration
public class TomcatVirtualThreadsConfig {

    @Bean
    public TomcatProtocolHandlerCustomizer<?> tomcatProtocolHandlerCustomizer() {
        return protocolHandler -> {
            // Hardened Fix: Use the type-safe thread factory structure to bypass compiler limits
            protocolHandler.setExecutor(Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory()));
        };
    }
}