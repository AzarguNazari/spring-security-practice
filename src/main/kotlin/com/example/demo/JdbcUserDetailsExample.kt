package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.persistence.*
import javax.sql.DataSource

@SpringBootApplication
class JdbcUserDetailsExample

fun main(args: Array<String>) {
	runApplication<JdbcUserDetailsExample>(*args)
}

@RestController
class HelloController(val jdbcUserDetailsManager: JdbcUserDetailsManager, val passwordEncoder: PasswordEncoder) {

	@GetMapping("/hello")
	fun hello() = "Hello"

	@PostMapping("/user")
	fun addUser(@RequestBody user: User) {
		user.pass = passwordEncoder.encode(user.password)
		jdbcUserDetailsManager.createUser(user)
	}
}



@Service
class SecurityConfig : WebSecurityConfigurerAdapter() {

	@Bean
	fun passwordEncoder() : PasswordEncoder = BCryptPasswordEncoder()

	@Bean
	override fun userDetailsService() : JdbcUserDetailsManager = JdbcUserDetailsManager(dataSource())

	fun dataSource() : DataSource{
		val dataSource = DriverManagerDataSource()
		dataSource.url = "jdbc:h2:mem:testdb"
		dataSource.username = "sa"
		dataSource.password = ""
		return dataSource
	}

	override fun configure(http: HttpSecurity) {
		http.headers().frameOptions().disable()
		http.httpBasic()
		http.csrf().disable()
		http.authorizeRequests().anyRequest().permitAll()
	}
}

data class User(var uname: String, var pass: String) : UserDetails{
	override fun getAuthorities() = mutableListOf(GrantedAuthority { "read" })
	override fun getPassword() = pass
	override fun getUsername() = uname
	override fun isAccountNonExpired() = true
	override fun isAccountNonLocked() = true
	override fun isCredentialsNonExpired() = true
	override fun isEnabled() = true
}

@Entity
@Table(name = "users")
data class UserData(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int = 0, val username: String = "", val password: String = "", val enabled : Boolean = true)

@Entity
@Table(name = "authorities")
data class AuthoritiesData(@Id @GeneratedValue(strategy = GenerationType.AUTO) val username: String = "", val authority: String = "")