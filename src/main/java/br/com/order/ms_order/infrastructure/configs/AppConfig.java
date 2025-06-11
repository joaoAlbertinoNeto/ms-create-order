package br.com.order.ms_order.infrastructure.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class AppConfig {


    @Bean
    public ModelMapper modelMap() {
        ModelMapper modelMap = new ModelMapper();
        return modelMap;
    }
}
