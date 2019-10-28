package com.cispact.app.ws.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cispact.app.ws.restappws.service.UserService;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{

	/*custom implementation of UserDetailsService */
	private final UserService userDetailsService; 
	private final BCryptPasswordEncoder  bCryptPasswordEncoder;
	
	public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf()
		.disable()
		.authorizeRequests()
		//only "/users" entry point is public if we try to access 403(Forbidden)
		.antMatchers(HttpMethod.POST,SecurityConstants.SIGN_UP_URL).permitAll()
		.anyRequest().authenticated().and()
		//added AuthenticationFilter
		.addFilter(getAuthenticationFilter())
		//added AuthorizationFilter
		.addFilter(new AuthorizationFilter(authenticationManager()))
		//not to creating session management  (session management stateless) 
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(bCryptPasswordEncoder);
	}
	
	//creating Custom URL default is "/login"
	//creates new instance of AuthenticationFilter(authenticationManager() and customized UserAuthentication URL
	public AuthenticationFilter getAuthenticationFilter() throws Exception {
	  AuthenticationFilter   authenticationFilter=	new AuthenticationFilter(authenticationManager());
	  authenticationFilter.setFilterProcessesUrl("/users/login");
	  return authenticationFilter;
	}
	
	
	
	
	
	
        
}
