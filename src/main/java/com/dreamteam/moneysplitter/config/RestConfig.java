package com.dreamteam.moneysplitter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RestConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setFilterProvider(new SimpleFilterProvider().setFailOnUnknownId(false));
        return mapper;
    }
}
