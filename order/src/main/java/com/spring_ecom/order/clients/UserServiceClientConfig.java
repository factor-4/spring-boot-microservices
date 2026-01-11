package com.spring_ecom.order.clients;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class UserServiceClientConfig {


    // ✅ Explicitly inject ONLY the load-balanced one
    @Bean
    public UserServiceClient userServiceClient(
            @Qualifier("loadBalancedBuilder") RestClient.Builder builder
    ) {
        RestClient restClient = builder
                .baseUrl("http://user-service")
                .defaultStatusHandler(HttpStatusCode::is4xxClientError,
                        ((request, response) -> Optional.empty()))
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);

        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(UserServiceClient.class);
    }
}