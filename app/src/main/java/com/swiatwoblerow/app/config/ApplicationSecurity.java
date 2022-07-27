package com.swiatwoblerow.app.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import com.swiatwoblerow.app.config.jwt.JWTAuthorizationFilter;
import com.swiatwoblerow.app.config.jwt.JwtAuthenticationEntryPoint;
import com.swiatwoblerow.app.service.CustomerServiceImpl;

@Configuration
@EnableWebSecurity(debug = true)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	@Qualifier("customerServiceImpl")
	private CustomerServiceImpl customerService;
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.authenticationProvider(authenticationProvider());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		
		http
			.authorizeRequests()
				.antMatchers(HttpMethod.POST,"/login").permitAll()
				.antMatchers(HttpMethod.GET,"/api/category/cars").permitAll()
				.antMatchers(HttpMethod.GET,"/api/offer/").permitAll()
				.antMatchers(HttpMethod.GET,"/api/home").permitAll()
				.antMatchers(HttpMethod.GET,"/error").permitAll()
			.anyRequest().authenticated().and()
			.cors().and()
			.csrf().disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);
			
		http.addFilterBefore(jwtTokenAuthorizationFilter(),UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Bean
	public JWTAuthorizationFilter jwtTokenAuthorizationFilter() {
		return new JWTAuthorizationFilter();
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
