package com.example.kapyedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class KapyshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(KapyshopApplication.class, args);
	}

	@GetMapping
	public String welcome(){
		return "<h1> Welcome To KapyEdu API Platform <small> powered by <a href='https://kapygenius.com'>KapyGenius</a> </small> </h1>";
	}

}
