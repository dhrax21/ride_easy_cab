package com.example.ride_easy.repositories;


import com.example.ride_easy.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RideRepository extends JpaRepository<Ride, Integer> {
	
	@Query("SELECT R FROM Ride R WHERE R.status=REQUESTED AND R.driver.ID=:driverId")
	public Ride getDriversCurrentRide(@Param("driverId") Integer driverId);

}
