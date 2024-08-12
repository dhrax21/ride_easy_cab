package com.example.ride_easy.repositories;


import com.example.ride_easy.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer>{

}
