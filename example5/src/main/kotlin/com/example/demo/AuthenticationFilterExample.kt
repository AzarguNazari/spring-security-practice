package com.example.demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@SpringBootApplication
class AuthenticationFilterExample

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


fun main(args: Array<String>) {
	runApplication<AuthenticationFilterExample>(*args)
}

@RestController
class HelloController{

	@GetMapping("/hello")
	fun hello() = "Hello"

}

@Order(1)
@Component
class SecurityConfig1 : WebSecurityConfigurerAdapter(){
	@Bean
	override fun authenticationManager(): AuthenticationManager {
		return super.authenticationManager()
	}
}

@Order(2)
@Component
class SecurityConfig2(val customFilter: CustomAuthenticationFilter, val customAuth: CustomAuthenticationProvider) : WebSecurityConfigurerAdapter(){

	override fun configure(auth: AuthenticationManagerBuilder) {
		auth.authenticationProvider(customAuth)
	}

	override fun configure(http: HttpSecurity) {
		http.addFilterAt(customFilter, BasicAuthenticationFilter::class.java)

		http.authorizeRequests().anyRequest().permitAll()
	}
}

@Component
class CustomAuthenticationFilter(val authenticationManager: AuthenticationManager) : OncePerRequestFilter() {

	override fun doFilterInternal(request: HttpServletRequest,
								  response: HttpServletResponse,
								  chain: FilterChain) {

		val authorization = request.getHeader("Authorization")
		val a = CustomAuthentication(authorization, null)

		try {
			val result: Authentication = authenticationManager.authenticate(a)
			if (result.isAuthenticated) {
				SecurityContextHolder.getContext().authentication = result
				chain.doFilter(request, response)
			} else {
				response.status = HttpServletResponse.SC_FORBIDDEN
			}
		} catch (e: AuthenticationException) {
			response.status = HttpServletResponse.SC_FORBIDDEN
		}
	}
}

class CustomAuthentication : UsernamePasswordAuthenticationToken {
	constructor(principal: Any?, credentials: Any?) : super(principal, credentials)
	constructor(principal: Any?, credentials: Any?, authorities: Collection<GrantedAuthority?>?) : super(principal, credentials, authorities)
}

@Component
class CustomAuthenticationProvider : AuthenticationProvider{

	@Value("\${key}")
	private val key = "abcde"

	override fun authenticate(authentication: Authentication): Authentication {
		if(authentication.name == key){
			return CustomAuthentication(null, null, null)
		}
		else{
			throw BadCredentialsException("Bad credential")
		}
	}

	override fun supports(authProvider: Class<*>) = CustomAuthentication::class.java == authProvider::class.java

}