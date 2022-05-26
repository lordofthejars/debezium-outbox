package com.example.orderservicesb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class OrderServiceSbApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceSbApplication.class, args);
	}

}
