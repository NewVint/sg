package com.sg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication

public class SgApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgApplication.class, args);
	}

}
