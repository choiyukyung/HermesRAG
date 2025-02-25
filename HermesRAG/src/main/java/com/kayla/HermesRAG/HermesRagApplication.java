package com.kayla.HermesRAG;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HermesRagApplication {

	public static void main(String[] args) {
		SpringApplication.run(HermesRagApplication.class, args);
	}

}
