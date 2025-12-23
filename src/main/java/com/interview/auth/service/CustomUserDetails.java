package com.interview.auth.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.interview.auth.entities.UserInfo;
import com.interview.auth.entities.UserRole;

public class CustomUserDetails extends UserInfo implements UserDetails{

	    private String username;

	    private String password;
	    
	    Collection<? extends GrantedAuthority> authorities;
	    
	    public CustomUserDetails(UserInfo userinfo) {
	    	this.username = userinfo.getUsername();
	    	this.password = userinfo.getPassword();
	    	List<GrantedAuthority> auths = new ArrayList<>();
	    	for(UserRole roles : userinfo.getRoles()) {
	    		// creating this object because GrantedAuthority is a interface so we have to create a custom class which implements that interface ,so spring gave a 
	    		// in built wrapper class for us just to serve the requirement.
	    		auths.add(new SimpleGrantedAuthority(roles.getName().toUpperCase()));
	    	}
	    	this.authorities=auths;
	    }
	    
	    @Override
		public Collection<? extends GrantedAuthority> getAuthorities(){
	    	return this.authorities;
	    }
	    
	    @Override
	    public String getPassword() {
	        return password;
	    }

	    @Override
	    public String getUsername() {
	        return username;
	    }

	    @Override
	    public boolean isAccountNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isAccountNonLocked() {
	        return true;
	    }

	    @Override
	    public boolean isCredentialsNonExpired() {
	        return true;
	    }

	    @Override
	    public boolean isEnabled() {
	        return true;
	    }
	    
}
