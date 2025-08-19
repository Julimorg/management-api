package com.example.managementapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ManagementApiApplication {

    public static void main(String[] args) {
//        Dotenv dotenv = Dotenv.configure().load();
//        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        SpringApplication.run(ManagementApiApplication.class, args);
    }

}
