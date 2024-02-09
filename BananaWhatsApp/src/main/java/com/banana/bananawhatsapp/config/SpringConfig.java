package com.banana.bananawhatsapp.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan({"com.banana.bananawhatsapp.persistencia", "com.banana.bananawhatsapp.controladores", "com.banana.bananawhatsapp.servicios"})
@PropertySource("classpath:application.properties")
@EntityScan("com.banana.bananawhatsapp.modelos")
@EnableAutoConfiguration
@EnableJpaRepositories("com.banana.bananawhatsapp.persistencia")
public class SpringConfig {
}
