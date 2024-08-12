package com.example.ride_easy.controllers;


import com.example.ride_easy.models.Notification;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
	
	
	   @MessageMapping("/notification")
	   @SendTo("/topic/notification")
	   @SendToUser("/user/username")
	   public Notification send(Notification notification) {
	       return notification;
	   }
	


}
