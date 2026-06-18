package com.example.ms_profesores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsProfesoresApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsProfesoresApplication.class, args);
	}

}
