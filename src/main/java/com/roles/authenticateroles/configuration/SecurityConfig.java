package com.roles.authenticateroles.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.roles.authenticateroles.handlers.CutomAuthEntryPoint;
import com.roles.authenticateroles.handlers.LoginFailureHandler;
import com.roles.authenticateroles.handlers.LoginSuccessHandler;
import com.roles.authenticateroles.user.CustomUserDetailsService;
import com.roles.authenticateroles.user.User;

@Configuration
@EnableAutoConfiguration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired CustomUserDetailsService userDetailsService;
	
	@Bean
	UserDetailsService userDetailsService() {
		return userDetailsService;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public User user() {
		return new User();
	}
	
	
	
	@Bean 
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
		//.sessionManagement()
		//.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		//.and()
	//	.securityContext().securityContextRepository(new CustomSecurityContext())
	//	.and()
		.authorizeHttpRequests()
		.requestMatchers("/user/**").hasRole("USER")
		.requestMatchers("/admin/**").hasRole("ADMIN")
		.requestMatchers("/api/user/**").hasRole("USER")
		.requestMatchers("/api/admin/**").hasRole("ADMIN")
		.anyRequest().permitAll()
		.and()
		.formLogin()
		.loginPage("/login")
		.loginProcessingUrl("/login")
				.defaultSuccessUrl("/user")
		.successHandler(new LoginSuccessHandler())
		.failureHandler(new LoginFailureHandler())
		.permitAll()
		.and()
		.logout().logoutSuccessUrl("/login").permitAll()
		.and()
		.userDetailsService(userDetailsService())
		//.authenticationManager(authenticationManager())
		//.authenticationProvider(authenticationProvider())
		.exceptionHandling().authenticationEntryPoint(new CutomAuthEntryPoint());
		//.and()

	//	http.addFilterAt(new UserPasswordFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean 
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth=new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetailsService());
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
		
		
	}
	/*
	 * @Primary
	 * 
	 * @Bean public AuthenticationManager
	 * authenticationManager(AuthenticationManagerBuilder builder) throws Exception
	 * { builder.authenticationProvider(authenticationProvider()); return
	 * builder.build(); }
	 */
	 

}
