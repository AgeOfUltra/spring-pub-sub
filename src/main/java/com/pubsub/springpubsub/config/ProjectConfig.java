package com.pubsub.springpubsub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.web.servlet.View;

@Configuration
public class ProjectConfig {
    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster (){
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();

        eventMulticaster.setErrorHandler(error -> {
            System.out.println("Error in event listener"+error.getMessage());
        });

        return eventMulticaster;
    }
}
