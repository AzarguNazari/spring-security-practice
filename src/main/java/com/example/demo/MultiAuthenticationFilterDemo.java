package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MultiAuthenticationFilterDemo {

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


	public static void main(String[] args) {
		SpringApplication.run(MultiAuthenticationFilterDemo.class, args);
	}

}
