package com.example.ride_easy.controllers;


import com.example.ride_easy.exceptions.DriverException;
import com.example.ride_easy.models.Driver;
import com.example.ride_easy.models.Ride;
import com.example.ride_easy.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {
	
	@Autowired
	private DriverService driverService;
	
	@GetMapping("/profile")
	public ResponseEntity<Driver> getReqDriverProfileHandler(@RequestHeader("Authorization") String jwt) throws DriverException {

		// ok
		
		Driver driver = driverService.getReqDriverProfile(jwt);
		
		return new ResponseEntity<Driver>(driver,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{driverId}/current_ride")
	public ResponseEntity<Ride> getDriversCurrentRideHandler(@PathVariable Integer driverId) throws DriverException{
		
		Ride ride=driverService.getDriversCurrentRide(driverId);
		
		return new ResponseEntity<Ride>(ride,HttpStatus.ACCEPTED);
	}

	@GetMapping("/{driverId}/allocated")
	public ResponseEntity<List<Ride>> getAllocatedRidesHandler(@PathVariable Integer driverId) throws DriverException{
		List<Ride> rides=driverService.getAllocatedRides(driverId);
		
		return new ResponseEntity<>(rides,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/rides/completed")
	public ResponseEntity<List<Ride>> getcompletedRidesHandler(@RequestHeader("Authorization") String jwt) throws DriverException{
		
		Driver driver = driverService.getReqDriverProfile(jwt);
		
		List<Ride> rides=driverService.completedRids(driver.getId());
		
		return new ResponseEntity<>(rides,HttpStatus.ACCEPTED);
	}

}
