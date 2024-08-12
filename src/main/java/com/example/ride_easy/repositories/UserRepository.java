package com.example.ride_easy.repositories;


import com.example.ride_easy.models.Ride;
import com.example.ride_easy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmail(String email);
	
	@Query("SELECT R FROM Ride R where R.status=COMPLETED AND R.user.Id=:userId ORDER BY R.endTime DESC")
	public List<Ride> getCompletedRides(@Param("userId")Integer userId);
}
