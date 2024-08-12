package com.example.ride_easy.controllers;


import com.example.ride_easy.controllers.mapper.DtoMapper;
import com.example.ride_easy.dtos.RideDTO;
import com.example.ride_easy.exceptions.DriverException;
import com.example.ride_easy.exceptions.RideException;
import com.example.ride_easy.exceptions.UserException;
import com.example.ride_easy.models.Driver;
import com.example.ride_easy.models.Ride;
import com.example.ride_easy.models.User;
import com.example.ride_easy.requests.RideRequest;
import com.example.ride_easy.requests.StartRideRequest;
import com.example.ride_easy.responses.MessageResponse;
import com.example.ride_easy.services.DriverService;
import com.example.ride_easy.services.RideService;
import com.example.ride_easy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
public class RideController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RideService rideService;
	
	@Autowired
	private DriverService driverService;
	
	@PostMapping("/request")
	public ResponseEntity<RideDTO> userRequestRideHandler(@RequestBody RideRequest rideRequest, @RequestHeader("Authorization") String jwt) throws UserException, DriverException{
		
		User user =userService.findUserByToken(jwt);
		
		Ride ride=rideService.requestRide(rideRequest, user);
		
		RideDTO rideDto= DtoMapper.toRideDto(ride);
		
		return new ResponseEntity<>(rideDto,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{rideId}/accept")
	public ResponseEntity<MessageResponse> acceptRideHandler(@PathVariable Integer rideId) throws UserException, RideException{
		
		rideService.acceptRide(rideId);
		
		MessageResponse res=new MessageResponse("Ride Accepted By Driver");
		
		return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{rideId}/decline")
	public ResponseEntity<MessageResponse> declineRideHandler(@RequestHeader("Authorization") String jwt, @PathVariable Integer rideId) 
			throws UserException, RideException, DriverException {
		
		Driver driver = driverService.getReqDriverProfile(jwt);
		
		rideService.declineRide(rideId, driver.getId());
		
		MessageResponse res=new MessageResponse("Ride decline By Driver");
		
		return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{rideId}/start")
	public ResponseEntity<MessageResponse> rideStartHandler(@PathVariable Integer rideId, @RequestBody StartRideRequest req) throws UserException, RideException{
		
		rideService.startRide(rideId,req.getOtp());
		
		MessageResponse res=new MessageResponse("Ride is started");
		
		return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
	}

	@PutMapping("/{rideId}/complete")
	public ResponseEntity<MessageResponse> rideCompleteHandler(@PathVariable Integer rideId) throws UserException, RideException{
		
		rideService.completeRide(rideId);
		
		MessageResponse res=new MessageResponse("Ride Is Completed Thank You For Booking Cab");
		
		return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{rideId}")
	public ResponseEntity<RideDTO> findRideByIdHandler(@PathVariable Integer rideId, @RequestHeader("Authorization") String jwt) throws UserException, RideException{
		
		User user =userService.findUserByToken(jwt);
	
		Ride ride =rideService.findRideById(rideId);

		
		RideDTO rideDto=DtoMapper.toRideDto(ride);
		
		
		return new ResponseEntity<RideDTO>(rideDto,HttpStatus.ACCEPTED);
	}

//	complete all ride apis
}
