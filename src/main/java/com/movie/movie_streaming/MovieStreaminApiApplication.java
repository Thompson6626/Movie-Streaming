package com.movie.movie_streaming;

import com.movie.movie_streaming.Utilities.init.InitObjects;
import com.movie.movie_streaming.movie.MovieRepository;
import com.movie.movie_streaming.movie.MovieService;
import com.movie.movie_streaming.role.Role;
import com.movie.movie_streaming.user.User;
import com.movie.movie_streaming.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
	public CommandLineRunner runner(UserRepository userRepository,MovieService movieService) {
		return args -> {
			userRepository.save(InitObjects.admin());
			InitObjects.movies().forEach(movieService::saveMovie);
		};
	}


}
