package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class Example1SecurityApplication{

	@Bean
	fun userDetails(): UserDetailsService {
		val userManager = InMemoryUserDetailsManager()

		val user1 = User.withUsername("user1").password("password1").authorities("read").build()
		val user2 = User.withUsername("user2").password("password2").authorities("read").build()

		userManager.createUser(user1)
		userManager.createUser(user2)
		return userManager
	}

	@Bean
	fun passwordEncoder() : PasswordEncoder = NoOpPasswordEncoder.getInstance()


}

fun main(args: Array<String>) {
	runApplication<Example1SecurityApplication>(*args)
}

@RestController
class RestEndpoint{

	@GetMapping("/hello")
	fun hello() = "Hello"

}