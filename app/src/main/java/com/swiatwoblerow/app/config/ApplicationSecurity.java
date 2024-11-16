package com.swiatwoblerow.app.config;


import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.swiatwoblerow.app.config.jwt.JWTAuthorizationFilter;
import com.swiatwoblerow.app.config.jwt.JwtAuthenticationEntryPoint;
import com.swiatwoblerow.app.service.CustomerServiceImpl;

@Configuration
@EnableWebSecurity(debug = true)
public class ApplicationSecurity{
	
	private CustomerServiceImpl customerService;
	
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	public ApplicationSecurity(CustomerServiceImpl customerService,
			JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
		this.customerService = customerService;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		
		http
			.authorizeHttpRequests((authorize) -> authorize
							.requestMatchers(HttpMethod.GET, "/actuator").permitAll()
							.requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
				.requestMatchers(HttpMethod.POST,"/login").permitAll()
				.requestMatchers(HttpMethod.GET,"/products/**").permitAll()
					.requestMatchers(HttpMethod.GET,"/conditions/**").permitAll()
				.requestMatchers(HttpMethod.GET,"/countries/**").permitAll()
				.requestMatchers(HttpMethod.POST,"/countries").hasAnyRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE,"/countries/**").hasAnyRole("ADMIN")
				.requestMatchers(HttpMethod.GET,"/categories/**").permitAll()
				.requestMatchers(HttpMethod.POST,"/categories").hasAnyRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE,"/categories/**").hasAnyRole("ADMIN")
//				.requestMatchers(HttpMethod.GET,"/customers/**").hasAnyRole("ADMIN","MODERATOR")
				.requestMatchers(HttpMethod.GET,"/error").permitAll().anyRequest().authenticated()
			)
			.cors((cors) -> cors.configurationSource(corsConfigurationSource())
			)
			.csrf((csrf) -> csrf.disable()
			)
			.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.exceptionHandling((exception) -> exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			);
			
		http.addFilterBefore(jwtAuthorizationFilter(),UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.applyPermitDefaultValues();
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.setAllowedHeaders(List.of("*"));
		UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
		corsSource.registerCorsConfiguration("/**", corsConfiguration);
		return corsSource;
	}
	
	@Bean
	public JWTAuthorizationFilter jwtAuthorizationFilter() {
		return new JWTAuthorizationFilter();
		}
	
	@Bean
	public AuthenticationManager authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customerService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return new ProviderManager(authenticationProvider);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
