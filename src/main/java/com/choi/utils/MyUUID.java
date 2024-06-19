package com.choi.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MyUUID {
    @Bean
    public String createUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
