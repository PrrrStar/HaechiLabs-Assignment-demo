package com.example.demo.configuration;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestClientConfiguration {

    private final String xHenesisSecret;
    private final String authorization;


    public RestClientConfiguration(@Value("${xHenesisSecret}") final String xHenesisSecret,
                                   @Value("${authorization}") final String authorization) {
        this.xHenesisSecret = xHenesisSecret;
        this.authorization = authorization;
    }


    /**
     * properties 에서 secret, authoriztion 값을 가져와 header에 추가해줍니다.
     * @return HttpEntity<String> 으로 반환합니다.
     */
    @Bean
    public HttpEntity<String> createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Henesis-Secret", xHenesisSecret);
        headers.add("Authorization", authorization);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        return httpEntity;
    }

    /**
     * restTemplate 을 빌드합니다.
     * @return builder.build()
     */
    @Bean
    public RestTemplate restTemplate() {

        HttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(150)
                .setMaxConnPerRoute(50)
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(3000);
        factory.setConnectTimeout(500);

        factory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(factory);

        return restTemplate;
    }

}