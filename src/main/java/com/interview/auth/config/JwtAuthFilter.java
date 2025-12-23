package com.interview.auth.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.interview.auth.service.JwtService;
import com.interview.auth.service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Component
@AllArgsConstructor
@Data
public class JwtAuthFilter extends OncePerRequestFilter{

//    private final UserDetailsService userDetailsService_1;
	// we are extending this because if there are multiple requests given by same user there is no need to apply filter 
	// for all , apply only once per user. 
	
	@Autowired 
	JwtService jwtService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

//    JwtAuthFilter(UserDetailsService userDetailsService_1) {
//        this.userDetailsService_1 = userDetailsService_1;
//    }
	
	@Override
	protected void doFilterInternal(HttpServletRequest request ,HttpServletResponse respose , FilterChain filterChain) throws IOException, ServletException {
		String authHeader = request.getHeader("Authorization");
		String token = null , username = null;
		if(authHeader != null && authHeader.startsWith("Bearer ")){
			token = authHeader.substring(7);
			username = jwtService.extractUsername(token);
		}
				
//	SecurityContextHolder.getContext().getAuthentication() == null	checking against multiple filters trying to set the context within the same single request.
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// if the username is not null from the token and securitycontext is null means first time the user is requesting so we need to set the securityContext
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if(jwtService.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken authenticationToken =  new UsernamePasswordAuthenticationToken(userDetails , null,userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}
		// after setting the context we need to pass to next filter , but in our case there are no other filters so  it will go to controller
		filterChain.doFilter(request, respose);
	}
}
