package com.spring_ecom.order.controllers;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class MessageController {

    @GetMapping("/message")
    @RateLimiter(name = "rateBreaker", fallbackMethod = "getMessageFallback")
    public String getMessage(){
        return "Hello order";
    }

    public String getMessageFallback(Exception e) {
        return "Hello Fallback";
    }
}
