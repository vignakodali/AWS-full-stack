package com.example.aws.springbootdynamodbcrud;

import org.springframework.scheduling.annotation.EnableScheduling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class SpringbootDynamodbCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootDynamodbCrudApplication.class, args);
	}

}
