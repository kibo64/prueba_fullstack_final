package com.example.ms_evaluaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsEvaluacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsEvaluacionesApplication.class, args);
	}

}
