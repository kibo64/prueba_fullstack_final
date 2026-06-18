package com.example.ms_asistencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsAsistenciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAsistenciaApplication.class, args);
	}

}
