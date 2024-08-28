package com.example.soundofmeme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.soundofmeme")
public class SoundOfMemeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoundOfMemeApplication.class, args);
	}

}
