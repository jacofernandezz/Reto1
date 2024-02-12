package com.banana.bananawhatsapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@Profile("prod")
@PropertySource("classpath:application-prod.properties")
public class ProdConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer configurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }
}
