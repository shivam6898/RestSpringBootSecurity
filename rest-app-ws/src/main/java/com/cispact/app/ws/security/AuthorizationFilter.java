package com.cispact.app.ws.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

//Triggers whenever User try to access end-points
public class AuthorizationFilter extends BasicAuthenticationFilter {

	public AuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}


	@Override
	protected void doFilterInternal(HttpServletRequest req
			,HttpServletResponse res
			,FilterChain chain
			)throws IOException,ServletException {
              
		//reading Authorization header
		String header=  req.getHeader(SecurityConstants.HEADER_STRING);

		if(header==null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
           
		//if UsernamePasswordAuthentication is successful then set to SecurityContextHolder
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

     //finds User based on token 
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		//reading token from header
		String token =request.getHeader(SecurityConstants.HEADER_STRING);

		if(token!=null) {
			//removing Bearer with empty String
			token =token.replaceAll(SecurityConstants.TOKEN_PREFIX,"");
              
			//parsing token and getting user
			String user=Jwts.parser()
					.setSigningKey(SecurityConstants.TOKEN_SECRET)
					.parseClaimsJws(token)
					.getBody()
					.getSubject();

			if(user !=null) {
				//Authenticate user
				return new UsernamePasswordAuthenticationToken(user, null,new ArrayList<>());
			}
			return null;
		}


		return null;

	}
}
