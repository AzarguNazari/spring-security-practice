package com.example.demo

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@SpringBootApplication
class SecurityWithJpa(val userRepository: UserRepository): ApplicationRunner{

	@Bean
	fun passwordEncoder() : PasswordEncoder = NoOpPasswordEncoder.getInstance()

	override fun run(args: ApplicationArguments) {
		val user1 = User(1, "john", "123")
		userRepository.save(user1)
		userRepository.findAll().forEach {
			println(it.username)
			println(it.password)
		}
	}
}

fun main(args: Array<String>) {
	runApplication<SecurityWithJpa>(*args)
}

@RestController
class HelloController{

	@GetMapping("/hello")
	fun hello() = "Hello"

}


@Entity
data class User(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int = 0, val username: String = "", val password: String = "")

interface UserRepository: JpaRepository<User, Int> {
	open fun findUserByUsername(username: String): Optional<User>
}

@Service
class JPAUserDetailsService(val userRepository: UserRepository) : UserDetailsService{
	override fun loadUserByUsername(username: String): UserDetails{
		val user = userRepository.findUserByUsername(username).orElseThrow { UsernameNotFoundException("User is not found") }
		println("User ${user.password} is found")
		return org.springframework.security.core.userdetails.User.withUsername(user.username).password(user.password).authorities("read").build()
	}
}
