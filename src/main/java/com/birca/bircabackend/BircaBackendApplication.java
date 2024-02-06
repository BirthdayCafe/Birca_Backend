package com.birca.bircabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BircaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BircaBackendApplication.class, args);
	}

}
