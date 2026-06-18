package com.example.ms_notas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsNotasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsNotasApplication.class, args);
	}

}
