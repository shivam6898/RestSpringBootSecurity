package com.cispact.app.ws.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cispact.app.ws.SpringApplicationContext;
import com.cispact.app.ws.restappws.model.request.UserLogin;
import com.cispact.app.ws.restappws.service.UserService;
import com.cispact.app.ws.shared.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager ;

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		super();
		this.authenticationManager = authenticationManager;
	}
	//this method triggers and Authenticate User by Username and password
	public Authentication attemptAuthentication(HttpServletRequest
			req,HttpServletResponse res) {

		try {
			UserLogin creds=new ObjectMapper().readValue(req.getInputStream(), UserLogin.class);
			return authenticationManager.authenticate( 
					new UsernamePasswordAuthenticationToken(creds.getEmail(),creds.getPassword(),new ArrayList<>())
					);

		} catch (IOException e) {
			throw new  RuntimeException(e);
		} 
	}

	//if User is Successfully Authenticated then successfulAuthentication is Trigerred
	protected  void successfulAuthentication(HttpServletRequest req,
			HttpServletResponse res,
			FilterChain chain,
			Authentication auth)
					throws IOException,ServletException{
		String userName=((User)auth.getPrincipal()).getUsername();

		//creating token 
		String token=Jwts.builder()
				.setSubject(userName)
				.setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512,SecurityConstants.TOKEN_SECRET)
				.compact();

		//adding userId to Header
		//first create ApplicationContext to get instance of UserService
		//beanname first letter small
		UserService userService =  (UserService) SpringApplicationContext.getBean("userServiceImpl");

		//getting the user and adding to response Header
		UserDto userDto= userService.getUser(userName);


		//add token to header request and give to client for every access user has to give token 
		res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX+token);
		res.addHeader("UserId",userDto.getUserId() );
	}












}
