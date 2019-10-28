package com.cispact.app.ws.restappws.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cispact.app.ws.exception.UserServiceException;
import com.cispact.app.ws.repository.UserRepository;
import com.cispact.app.ws.restappws.entity.UserEntity;
import com.cispact.app.ws.restappws.model.response.ErrorMessages;
import com.cispact.app.ws.restappws.service.UserService;
import com.cispact.app.ws.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder encoder;

	@Override
	public UserDto createUser(UserDto user) {

		// to avoid duplicate User 
		//find User by email if exists throw exception 
		if(userRepository.findUserByEmail(user.getEmail())!=null) {
			throw new RuntimeException("User already exists!");
		}

		UserEntity userEntity =new UserEntity();
		BeanUtils.copyProperties(user, userEntity);

		/*password eccoded before saving*/
		userEntity.setEncryptedPassword(encoder.encode(user.getPassword()));


		String generatedUserId=UUID.randomUUID().toString().substring(0, 10).toUpperCase();
		userEntity.setUserId(generatedUserId);


		//saved User
		UserEntity stroredUserDetails=userRepository.save(userEntity);

		UserDto returnValue =new UserDto();
		BeanUtils.copyProperties(stroredUserDetails,returnValue);
		return returnValue;
	}

	//this method is secured only returns userDetails on username and password
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity  userEntity=userRepository.findUserByEmail(email);
		if( userEntity==null)throw new UsernameNotFoundException(email);
		return new User(userEntity.getEmail(),userEntity.getEncryptedPassword() , new ArrayList<>());
	}


	//get userByEmail
	@Override
	public UserDto getUser(String email) {
		UserEntity  userEntity=userRepository.findUserByEmail(email);
		if( userEntity==null)throw new UsernameNotFoundException(email);
		UserDto returnValue =new UserDto();
		BeanUtils.copyProperties(userEntity,returnValue);
		return returnValue;
	}


	//get User by UserId
	@Override
	public UserDto getUserByUserId(String id) {
		UserDto returnValue =new UserDto();
		UserEntity  userEntity=userRepository.findByUserId(id);

		if(userEntity==null)
			throw new UsernameNotFoundException("User With"+id+"Not Found");

		BeanUtils.copyProperties(userEntity,returnValue);
		return returnValue;
	}



	@Override
	public UserDto updateUser(String userId, UserDto user) {

		UserDto returnValue =new UserDto();

		UserEntity userEntity= userRepository.findByUserId(userId);
		
		//if( userEntity==null)throw new UsernameNotFoundException("User with "+userId+"not found");
		if( userEntity==null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
         
		//updating only two fields
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());

		UserEntity updatedUserDetails=userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedUserDetails,returnValue);
		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity =userRepository.findByUserId(userId);
		 
		if(userEntity==null)throw 
		new UserServiceException(ErrorMessages.NO_RECORD_FOUND.name());
		
		
		userRepository.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		
		List<UserDto>  returnValue=new ArrayList<>();
		
		//creating pagable 
		Pageable pageable=PageRequest.of(page, limit);
		
	     //it returns Page<>
		Page<UserEntity> usersPage=userRepository.findAll(pageable);
		//to get in List form
		List<UserEntity> users=usersPage.getContent();
		
	   //	Copy  UserEntity to UserDto
		for (UserEntity userEntity : users) {
			UserDto userDto=new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
		     returnValue.add(userDto);
		}
		
		return returnValue;
	}

	

	




}
