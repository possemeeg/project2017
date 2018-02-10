package com.possemeeg.project2017.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {ServletWebServerFactoryAutoConfiguration.class})
public class App {
    public static void main(String[] args) {
      SpringApplication.run(App.class, args);
    }
    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        return new ServletWebServerFactory() {

            @Override
            public WebServer getWebServer(ServletContextInitializer... initializers) {
                return null;
            }
        };

    }
}
