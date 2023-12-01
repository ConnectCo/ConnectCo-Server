package com.connectCo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ConnectCoApplication {


	public static void main(String[] args) {

		SpringApplication.run(ConnectCoApplication.class, args);
	}

}
