package com.unir.hotels;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;



@SpringBootApplication
@Slf4j

public class MicroserviceHotelsApplication {

	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		// Retrieve execution profile from environment variable. If not present, default profile is selected.
		String profile = System.getenv("PROFILE");
		System.setProperty("spring.profiles.active", profile != null ? profile : "default");
		// Railway's internal interface takes some time to start. We wait for it to be ready.
		log.debug("Waiting fo Internal Interface to start...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		SpringApplication.run(MicroserviceHotelsApplication.class, args);
	}
}
