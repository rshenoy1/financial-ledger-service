package com.enterprise.ledger.config;

import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.Executors;

/**
 * Enterprise web server configuration overriding Tomcat's internal ingress pipelines.
 * Forces the embedded servlet container to run web requests exclusively on Virtual Threads.
 */
@Configuration
public class TomcatVirtualThreadsConfig {

    @Bean
    public TomcatProtocolHandlerCustomizer<?> tomcatProtocolHandlerCustomizer() {
        return protocolHandler -> {
            // Force Tomcat to utilize a virtual thread per task executor baseline mapping
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }
}