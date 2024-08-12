package com.example.ride_easy.services;



import com.example.ride_easy.exceptions.DriverException;
import com.example.ride_easy.models.Driver;
import com.example.ride_easy.models.Ride;
import com.example.ride_easy.requests.DriversSignupRequest;

import java.util.List;

public interface DriverService {
	
	public Driver registerDriver(DriversSignupRequest driverSignupRequest);
	
	public List<Driver> getAvailableDrivers(double pickupLatitude,
			double pickupLongitude,double radius, Ride ride);
	
	public Driver findNearestDriver(List<Driver> availableDrivers, 
			double picupLatitude, double picupLongitude);
	
	public Driver getReqDriverProfile(String jwt) throws DriverException;
	
	public Ride getDriversCurrentRide(Integer driverId) throws DriverException;
	
	public List<Ride> getAllocatedRides(Integer driverId) throws DriverException;
	
	public Driver findDriverById(Integer driverId) throws DriverException;
	
	public List<Ride> completedRids(Integer driverId) throws DriverException;
	
	
	



}
