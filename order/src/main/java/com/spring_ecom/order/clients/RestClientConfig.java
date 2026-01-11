package com.spring_ecom.order.clients;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    // ✅ Used by Eureka, Config Server, etc.
    @Bean
    @Primary
    public RestClient.Builder defaultRestClientBuilder() {
        return RestClient.builder();
    }

    // ✅ Used ONLY for service-to-service calls
    @Bean
    @LoadBalanced
    @Qualifier("loadBalancedBuilder")
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }
}
