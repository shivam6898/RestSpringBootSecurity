package com.cispact.app.ws.restappws.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cispact.app.ws.exception.UserServiceException;
import com.cispact.app.ws.restappws.model.request.UserDetails;
import com.cispact.app.ws.restappws.model.response.ErrorMessages;
import com.cispact.app.ws.restappws.model.response.OperationStatusModel;
import com.cispact.app.ws.restappws.model.response.RequestOperationName;
import com.cispact.app.ws.restappws.model.response.RequestOperationStatus;
import com.cispact.app.ws.restappws.model.response.UserRest;
import com.cispact.app.ws.restappws.service.UserService;
import com.cispact.app.ws.shared.dto.UserDto;

@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping(path="/{id}",produces={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public UserRest getUser(@PathVariable String id) {
		UserRest returnValue =new UserRest();

		UserDto userDto=userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto,returnValue);
		return returnValue;
	}



	//create user
	@PostMapping(produces= {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},
			consumes= {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE})
	public UserRest createUser(@RequestBody UserDetails userdetails) throws Exception {
		UserRest returnValue =new UserRest();

		//getting the error message from ErrorMessages class
		if(userdetails.getFirstName().isEmpty())throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

		UserDto userdto=new UserDto();
		//get the user details from userdetail and copy to userdto
		BeanUtils.copyProperties(userdetails, userdto);
		UserDto createdUser =userService.createUser(userdto);
		BeanUtils.copyProperties(createdUser, returnValue);
		return returnValue;
	}

	@PutMapping(path="/{id}",produces= {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},
			consumes= {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE})
	public UserRest  updateUser(@PathVariable String id,@RequestBody UserDetails userDetails ) {
		UserRest returnValue =new UserRest();
		UserDto userDto=new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto updatedUser=  userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);
		return returnValue;

	}

	@DeleteMapping(path="/{id}",produces= {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},
			consumes= {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE})
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel OperationStatusModel =new OperationStatusModel();
		OperationStatusModel.setOperationName(RequestOperationName.DELETE.name());
		userService.deleteUser(id);
		OperationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return OperationStatusModel;
	}

	/*get list<Users> in pagaeable form*/
	//  http://localhost:8080/users?page=0&limit=50
	@GetMapping(produces= {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE},
			consumes= {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_VALUE})
	public List<UserRest> getUsers(@RequestParam(value="page",defaultValue="0") int page
			,@RequestParam(value="limit",defaultValue="25") int limit)
	//default value for page starts with 0
	{
		List<UserRest> returnValue =new ArrayList<>();
		List<UserDto>  users =userService.getUsers(page,limit);

		//copy all users to   UserRest as response
		for (UserDto userDto : users) {
			UserRest userModel=new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}

		return returnValue;

	}

}
