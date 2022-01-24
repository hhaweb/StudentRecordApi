package com.student.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.student.service.impl.UserDetailsServiceImpl;

import io.jsonwebtoken.Jwts;

import org.slf4j.Logger;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsServiceImpl userDetailsService;
	


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}


	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JwtAuthenticationFilter authenticationJwtTokenFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http = http.cors().configurationSource(new CorsConfigurationSource() {

			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedHeaders(Collections.singletonList("*"));
				config.setAllowedMethods(Collections.singletonList("*"));
				config.addAllowedOrigin("http://172.104.40.242:4200");
				config.addAllowedOrigin("http://localhost:4200");

				config.setAllowCredentials(true);
				return config;
			}
		}).and().csrf().disable();
		
		// Set session management to stateless
		http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

		// Set unauthorized requests exception handler
		http = http.exceptionHandling().authenticationEntryPoint((request, response, ex) -> {
		//	logger.error("Unauthorized request - {}", ex.getMessage());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
		}).and();

		// Set permissions on endpoints
		http.authorizeRequests()
				// Swagger endpoints must be publicly accessible
				.antMatchers("/").permitAll()
				// Our public endpoints
				.antMatchers("/api/auth/login").permitAll()
				// Our private endpoints
				 .antMatchers("/api/student/save-student").access("hasRole('ROLE_SUPER_ADMIN')")
				 .antMatchers("/api/trainer/save-trainer").access("hasRole('ROLE_SUPER_ADMIN')")
				 .antMatchers("/api/course/save-course").access("hasRole('ROLE_SUPER_ADMIN')")
				 .antMatchers("/api/upload/**").access("hasRole('ROLE_SUPER_ADMIN')")
				 
				.anyRequest().authenticated();
//		 http
//         .logout(logout -> logout
//           .logoutUrl("/api/auth/logout")
//           .addLogoutHandler((request, response, auth) -> {
//        	   JwtAuthenticationFilter filter = authenticationJwtTokenFilter();
//        	   	String authToken = filter.parseJwt(request);
//      			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody().setExpiration(new Date(2020,1,1));
//  
//      			Date dd = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody().getExpiration();
//      			Date ss = new Date();
//      			String aa = "a";
//           })
//         );
		// Add JWT token filter
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
