package com.movie.movie_streaming.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.movie.movie_streaming.role.Permission.*;
import static com.movie.movie_streaming.role.Role.ADMIN;
import static com.movie.movie_streaming.role.Role.MANAGER;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig{

    private static final String[] WHITE_LIST = {
            "/auth/**",
            "/actors/**",
            "/directors/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST)
                            .permitAll()
                            .requestMatchers(POST, "/movies/*/comments").authenticated()
                            .requestMatchers("/movies/**").permitAll()
                            .requestMatchers("/manager/**").hasAnyRole(ADMIN.name(),MANAGER.name())
                            .requestMatchers(POST,"/manager/**").hasAnyAuthority(ADMIN_CREATE.name(),MANAGER_CREATE.name())
                            .requestMatchers(PUT,"/manager/**").hasAnyAuthority(ADMIN_UPDATE.name(),MANAGER_UPDATE.name())
                            .requestMatchers(DELETE,"/manager/**").hasAnyAuthority(ADMIN_DELETE.name(),MANAGER_DELETE.name())
                            .requestMatchers("/admin/**").hasRole(ADMIN.name())
                            .requestMatchers(PUT,"/admin/**").hasAuthority(ADMIN_UPDATE.name())
                            .anyRequest()
                            .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
