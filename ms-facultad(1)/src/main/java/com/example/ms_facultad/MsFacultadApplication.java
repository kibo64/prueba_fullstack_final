package com.example.ms_facultad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsFacultadApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsFacultadApplication.class, args);
	}

}
