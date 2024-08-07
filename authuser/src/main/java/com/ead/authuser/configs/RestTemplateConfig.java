package com.ead.authuser.configs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        final int TIMEOUT = 5000;

        return builder.
                setConnectTimeout(Duration.ofMillis(TIMEOUT)).
                setReadTimeout(Duration.ofMillis(TIMEOUT)).
                build();
    }
}
