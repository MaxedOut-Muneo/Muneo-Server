package com.example.muneoserver.domain.ai.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class AiClientConfig {

    @Bean
    public RestTemplate aiRestTemplate(AiProperties aiProperties) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(aiProperties.connectTimeoutMs());
        requestFactory.setReadTimeout(aiProperties.readTimeoutMs());
        return new RestTemplate(requestFactory);
    }
}
