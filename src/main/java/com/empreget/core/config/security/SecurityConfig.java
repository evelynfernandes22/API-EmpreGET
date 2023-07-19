package com.empreget.core.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private SecurityFilter securityFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		
		return httpSecurity
				.csrf().disable()
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.cors()
				.and()
//				.authorizeHttpRequests(authorize -> authorize
//						.antMatchers(HttpMethod.POST, "/auth/login/**", "/prestadores/**", "/clientes/**").permitAll()
//						.antMatchers(HttpMethod.DELETE, "/prestadores/**", "/clientes/**","/os/**" ).hasRole("ADMIN")
//						.antMatchers(HttpMethod.GET, "/prestadores/**").hasAnyRole("CLIENTE", "PRESTADOR")
//						.antMatchers(HttpMethod.PUT, "/prestadores/**").hasRole("PRESTADOR")
//						.antMatchers("/prestadores/**").hasRole("PRESTADOR")
//						.antMatchers("/clientes/**").hasRole("CLIENTE")
//				        .antMatchers(HttpMethod.PUT, "/clientes/**").hasRole("CLIENTE")
//				        .anyRequest().authenticated()
//						)
				.addFilterBefore(securityFilter,UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
		
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
