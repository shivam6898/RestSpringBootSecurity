package com.cispact.app.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class RestAppWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestAppWsApplication.class, args);
	}


	@Bean public BCryptPasswordEncoder bCryptPasswordEncoder() { return new
			BCryptPasswordEncoder();

	}


	/*creating the ApplicationContext to getBean without Autowired 
	  	ex:- UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
	 */

	@Bean
	public SpringApplicationContext  SpringApplicationContext() {
		return new SpringApplicationContext();

	}  

	/*
	 * @Bean("AppProperties") public AppProperties getAppproperties() { return new
	 * AppProperties();
	 * 
	 * }
	 */
}
