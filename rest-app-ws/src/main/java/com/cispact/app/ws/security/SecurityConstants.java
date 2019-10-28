package com.cispact.app.ws.security;

public class SecurityConstants {

	
	public static final long EXPIRATION_TIME=864000000;
	public static final String TOKEN_PREFIX="Bearer ";
	public static final String HEADER_STRING="Authorization";
	//public static String SIGN_UP_URL="/users";
	//public static String TOKEN_SECRET="jf9i4jgu83nfl0";
	
	
	public static final String SIGN_UP_URL="/users";
	public static final String TOKEN_SECRET="jf9i4jgu83nfl0";
	
	// Getting  instance of AppProperties from ApplicationContext
	/*
	 * public static String getTokenSecret() { AppProperties appProperties
	 * =(AppProperties)SpringApplicationContext.getBean("AppProperties"); return
	 * appProperties.getTokenSecret();
	 * 
	 * }
	 * 
	 * 
	 * 
	 * public static String getTokenSignUpUrl() { AppProperties appProperties
	 * =(AppProperties)SpringApplicationContext.getBean("AppProperties"); return
	 * appProperties.getSignUpUrl();
	 * 
	 * }
	 */
}
