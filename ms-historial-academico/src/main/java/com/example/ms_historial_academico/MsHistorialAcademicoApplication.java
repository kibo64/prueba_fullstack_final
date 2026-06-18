package com.example.ms_historial_academico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsHistorialAcademicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsHistorialAcademicoApplication.class, args);
	}

}
