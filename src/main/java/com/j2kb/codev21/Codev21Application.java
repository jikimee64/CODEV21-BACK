package com.j2kb.codev21;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Codev21Application {

	public static void main(String[] args) {
		SpringApplication.run(Codev21Application.class, args);
	}

}
