package com.example.ride_easy.configs;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class AppConfig  {


	@Bean
	public SecurityFilterChain securityConfigration(HttpSecurity http)
			throws Exception {

        http.authorizeHttpRequests().anyRequest().permitAll();
        return http.build();

//		http.sessionManagement()
//		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//		.and()
//		.authorizeHttpRequests()
//				.requestMatchers(HttpMethod.GET,"/swagger-ui").permitAll()
//				.requestMatchers(HttpMethod.GET,"/api/").permitAll()
//		.requestMatchers(HttpMethod.POST,"/api/auth/**").permitAll()
//		.requestMatchers(HttpMethod.GET,"/swagger-ul/**","/").permitAll()
//		.requestMatchers(HttpMethod.GET,"/ws/**").permitAll()
//		.anyRequest().authenticated()
//		.and()
//		.addFilterBefore(new JwtTokenValidationFilter(),
//				BasicAuthenticationFilter.class)
//
//		.csrf().disable()
//		.cors()
//		.configurationSource(new CorsConfigurationSource() {
//
//			@Override
//			public CorsConfiguration getCorsConfiguration
//			(HttpServletRequest request) {
//
//				CorsConfiguration cfg = new CorsConfiguration();
//
//				cfg.setAllowedOrigins(Arrays.asList(
//
//						"http://localhost:3000",
//						"http://localhost:3001",
//						"https://whatsaap-india.vercel.app"));
//				//cfg.setAllowedMethods(Arrays.asList("GET", "POST","DELETE","PUT"));
//				cfg.setAllowedMethods(Collections.singletonList("*"));
//				cfg.setAllowCredentials(true);
//				cfg.setAllowedHeaders(Collections.singletonList("*"));
//				cfg.setExposedHeaders(Arrays.asList("Authorization"));
//				cfg.setMaxAge(3600L);
//				return cfg;
//
//			}
//		}).and().httpBasic().and().formLogin();
//
//		return http.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}



}
