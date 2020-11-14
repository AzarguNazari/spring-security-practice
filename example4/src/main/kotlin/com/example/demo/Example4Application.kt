package com.example.demo

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class Example4Application{

	@GetMapping("/hello")
	fun hello() = "Hello"

}

fun main(args: Array<String>) {
	runApplication<Example4Application>(*args)
}



@Component
class SecurityConfig{

	@Bean
	fun userDetailsService() : UserDetailsService{
		val userDetails = InMemoryUserDetailsManager()

		val ahmad = User.withUsername("ahmad").password("123").authorities("read").build()

		userDetails.createUser(ahmad)

		return userDetails
	}

	@Bean
	fun passwordEncoder() : PasswordEncoder = NoOpPasswordEncoder.getInstance()
}

class CustomAuthenticationProvider(val passwordEncoder: PasswordEncoder, val userDetailsService: UserDetailsService) : AuthenticationProvider {

	val logger = LoggerFactory.getLogger(this.javaClass)

	override fun authenticate(authentication: Authentication): Authentication {

		val username = authentication.name
		val password = authentication.credentials

		val user = userDetailsService.loadUserByUsername(username)

		if(passwordEncoder.matches(password.toString(), user.password)){
			return UsernamePasswordAuthenticationToken(username, password, user.authorities)
		}

		throw BadCredentialsException("Bad authenication")
	}

	override fun supports(authType: Class<*>?): Boolean = authType?.javaClass  == UsernamePasswordAuthenticationToken::class.java

}