package com.cispact.app.ws.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.cispact.app.ws.restappws.model.response.CustomErrorMessage;

@ControllerAdvice
public class AppExceptionHandler  {

	//this method can handle multile exceptions by , seperator
	//but now it is handling UserServiceException
	@ExceptionHandler(value= {UserServiceException.class})
	public ResponseEntity<Object> handleUserServiceException(UserServiceException ex
			                                                 ,WebRequest request   
			                                                 )
	{
		/*
		 * custom exception looks like
		 * "timestamp": "2019-10-24T15:15:28.194+0000", "message":
		 * "Missing required field please check the documentation"
		 */
		CustomErrorMessage customErrorMessage =new CustomErrorMessage(new Date(),ex.getMessage());
		
		return new ResponseEntity<>(customErrorMessage    //ex
				                    ,new HttpHeaders()
				                    ,HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
}
