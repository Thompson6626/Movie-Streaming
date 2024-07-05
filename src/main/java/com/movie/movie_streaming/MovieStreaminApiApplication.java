package com.movie.movie_streaming;

import com.movie.movie_streaming.role.Role;
import com.movie.movie_streaming.user.User;
import com.movie.movie_streaming.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@SpringBootApplication
public class MovieStreaminApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieStreaminApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(UserRepository userRepository) {
		return args -> userRepository.save(
				User.builder()
						.username("admin")
						.email("adminemail@gmail.com")
						.password(new BCryptPasswordEncoder().encode("password1"))
						.createdDate(LocalDateTime.now())
						.accountLocked(false)
						.enabled(true)
						.role(Role.ADMIN)
						.build());
	}


}
