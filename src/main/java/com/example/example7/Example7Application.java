package com.example.example7;

import com.example.example7.entities.Otp;
import com.example.example7.entities.User;
import com.example.example7.repositories.OtpRepository;
import com.example.example7.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Example7Application implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(Example7Application.class, args);
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OtpRepository otpRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		User user1 = new User();
		user1.setUsername("ahmad");
		user1.setPassword("1234");

		userRepository.save(user1);

		Otp otp1 = new Otp();
		otp1.setOtp("abcde");
		otp1.setUsername("ahmad");

		otpRepository.save(otp1);
	}
}

