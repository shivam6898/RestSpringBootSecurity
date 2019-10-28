package com.cispact.app.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cispact.app.ws.restappws.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
	
	//use Rt that class which contains @Entity 
	//findBy______(variable name) argument (Variable)
	public UserEntity findUserByEmail(String email);
	
	public UserEntity findByUserId(String userId);
	
	
}
