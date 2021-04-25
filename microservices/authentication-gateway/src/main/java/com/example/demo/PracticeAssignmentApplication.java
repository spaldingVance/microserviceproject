package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude=SecurityAutoConfiguration.class)
@EnableDiscoveryClient
public class PracticeAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(PracticeAssignmentApplication.class, args);
	}



}
