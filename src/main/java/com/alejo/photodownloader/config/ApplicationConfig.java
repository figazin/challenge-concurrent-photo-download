package com.alejo.photodownloader.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ApplicationConfig {

    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors();

    @Bean
    public ThreadPoolExecutor executor() {
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(POOL_SIZE);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
