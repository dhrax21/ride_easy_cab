package com.example.ride_easy.services;


import com.example.ride_easy.exceptions.DriverException;
import com.example.ride_easy.exceptions.RideException;
import com.example.ride_easy.models.Driver;
import com.example.ride_easy.models.Ride;
import com.example.ride_easy.models.User;
import com.example.ride_easy.requests.RideRequest;

public interface RideService {
	
	
	public Ride requestRide(RideRequest rideRequest, User user) throws DriverException;
	
	public Ride createRideRequest(User user, Driver nearesDriver,
			double picupLatitude,double pickupLongitude,
			double destinationLatitude,double destinationLongitude,
			String pickupArea,String destinationArea);
	
	public void acceptRide(Integer rideId) throws RideException;
	
	public void declineRide(Integer rideId, Integer driverId) throws RideException;
	
	public void startRide(Integer rideId,int opt) throws RideException;
	
	public void completeRide(Integer rideId) throws RideException;
	
	public void cancleRide(Integer rideId) throws RideException;
	
	public Ride findRideById(Integer rideId) throws RideException;

}
