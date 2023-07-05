package com.grilo.todoservice.architecture.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperBean {
    //soute
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
