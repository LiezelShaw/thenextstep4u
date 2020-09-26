package com.thenextstep4u.blogging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BloggingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BloggingApplication.class, args);
	}

}
