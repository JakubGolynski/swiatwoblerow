package com.swiatwoblerow.app.config;


import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.swiatwoblerow.app.config.jwt.JWTAuthorizationFilter;
import com.swiatwoblerow.app.config.jwt.JwtAuthenticationEntryPoint;
import com.swiatwoblerow.app.service.CustomerServiceImpl;

@Configuration
@EnableWebSecurity(debug = true)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
	
	private CustomerServiceImpl customerService;
	
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	
	public ApplicationSecurity(CustomerServiceImpl customerService,
			JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
		this.customerService = customerService;
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		http
			.authorizeRequests()
				.antMatchers(HttpMethod.POST,"/login").permitAll()
				.antMatchers(HttpMethod.GET,"/products/**").permitAll()
				.antMatchers(HttpMethod.GET,"/countries/**").permitAll()
				.antMatchers(HttpMethod.POST,"/countries").hasAnyRole("ADMIN")
				.antMatchers(HttpMethod.DELETE,"/countries/**").hasAnyRole("ADMIN")
				.antMatchers(HttpMethod.GET,"/categories/**").permitAll()
				.antMatchers(HttpMethod.POST,"/categories").hasAnyRole("ADMIN")
				.antMatchers(HttpMethod.DELETE,"/categories/**").hasAnyRole("ADMIN")
				.antMatchers(HttpMethod.GET,"/customers/**").hasAnyRole("ADMIN","MODERATOR")
				.antMatchers(HttpMethod.GET,"/error").permitAll()
			.anyRequest().authenticated().and()
			.cors().configurationSource(corsConfigurationSource()).and()
			.csrf().disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
			
		http.addFilterBefore(jwtAuthorizationFilter(),UsernamePasswordAuthenticationFilter.class);
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
	
	@Override
	@Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customerService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
