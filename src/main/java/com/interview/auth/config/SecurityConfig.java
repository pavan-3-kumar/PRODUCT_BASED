package com.interview.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.interview.auth.AuthApplication;
import com.interview.auth.Repo.UserRepository;
import com.interview.auth.service.UserDetailsServiceImpl;

import lombok.Data;

@Configuration
@EnableMethodSecurity
@Data
public class SecurityConfig {



    private final PasswordEncoder passwordEncoder;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    SecurityConfig(PasswordEncoder passwordEncoder,UserDetailsServiceImpl userDetailsServiceImpl) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }


    @Bean
    @Autowired
    UserDetailsService userDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return new UserDetailsServiceImpl(userRepository,passwordEncoder);
	}

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) {
    		return http
    				.csrf(AbstractHttpConfigurer::disable).cors(CorsConfigurer::disable)
    				.authorizeHttpRequests(auth -> auth
    						.requestMatchers("/auth/v1/login","/auth/v1/refreshToken", "/auth/v1/signup").permitAll()
    						.anyRequest().authenticated()
    				)
    				.sessionManagement(sess-> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
					.httpBasic(Customizer.withDefaults()) // set all the remaining default filters
	                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
					.authenticationProvider(authenticationProvider())
					.build();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
    	DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsServiceImpl);
    	daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
    	return daoAuthenticationProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration  config) {
    	return config.getAuthenticationManager();    	
    }
	
    
}
