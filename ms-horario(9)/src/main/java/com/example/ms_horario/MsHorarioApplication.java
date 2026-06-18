package com.example.ms_horario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsHorarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsHorarioApplication.class, args);
	}

}
