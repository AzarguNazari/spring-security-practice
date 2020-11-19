package com.example.example6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Example6Application {

	public static void main(String[] args) {
		SpringApplication.run(Example6Application.class, args);
	}

}

/*
					Spring Security works in a chain layers

					---------------------------				|----------------------
					|						  |				|					  |
					|	Authentication Filter |-------------|   Security Context  |
					|						  |				|					  |
					|--------------------------				|----------------------
								|
								|
								|
					---------------------------
					|						  |
					|	AuthenticationManager |
					|						  |
					|--------------------------
								|
								|							|----------------------|
								|							|	UserDetailsService |
					----------------------------------------|----------------------|
					|						   |
					|	AuthenticationProvider |-------------|---------------------|
					|						   |			 |  PasswordEncoder    |
					|--------------------------|			 |---------------------|


 */


