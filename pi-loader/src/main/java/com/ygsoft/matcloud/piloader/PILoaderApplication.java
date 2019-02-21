package com.ygsoft.matcloud.piloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PILoaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(PILoaderApplication.class, args);
	}
}
