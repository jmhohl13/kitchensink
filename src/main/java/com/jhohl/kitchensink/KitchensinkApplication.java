package com.jhohl.kitchensink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class KitchensinkApplication {

	public static void main(String[] args) {

		SpringApplication.run(KitchensinkApplication.class, args);
			}
	@GetMapping("/hello")
	public String sayHello() {
		return String.format("Hello World!");
	}
}
