package com.cispact.app.ws.restappws.service;


import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.cispact.app.ws.shared.dto.UserDto;

//implemented UserDetailsService(I) which has method loadUserByUsername
public interface UserService extends UserDetailsService {

	public UserDto createUser(UserDto user);

	public UserDto getUser(String email);

	public UserDto getUserByUserId(String id);

	public UserDto updateUser( String userId,UserDto  userDto);

	public void deleteUser(String userId);

	public List<UserDto> getUsers(int page, int limit);

}
