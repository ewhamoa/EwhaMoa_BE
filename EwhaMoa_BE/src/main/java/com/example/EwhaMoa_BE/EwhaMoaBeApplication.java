package com.example.EwhaMoa_BE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class EwhaMoaBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EwhaMoaBeApplication.class, args);
	}

}
