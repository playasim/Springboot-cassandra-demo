package com.wayz.ai;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations={"classpath:spring/applicationContext.xml"})
public class MainApp {
    public static void main(String[] args) {
        SpringApplication.run(MainApp.class, args);

    }
}
