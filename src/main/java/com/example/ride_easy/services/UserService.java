package com.example.ride_easy.services;



import com.example.ride_easy.exceptions.UserException;
import com.example.ride_easy.models.Ride;
import com.example.ride_easy.models.User;

import java.util.List;


public interface UserService {
	
	public User createUser(User user) throws UserException;
	
	public User getReqUserProfile(String token) throws UserException;
	
	public User findUserById(Integer Id) throws UserException;
	
	public User findUserByEmail(String email) throws UserException;
	
	public User findUserByToken(String token) throws UserException;
	
	public List<Ride> completedRids(Integer userId) throws UserException;
	

}
