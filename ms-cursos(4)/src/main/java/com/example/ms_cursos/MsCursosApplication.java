package com.example.ms_cursos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsCursosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCursosApplication.class, args);
	}

}
