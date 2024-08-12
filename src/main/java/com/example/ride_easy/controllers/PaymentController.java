package com.example.ride_easy.controllers;

import com.example.ride_easy.enums.PaymentStatus;
import com.example.ride_easy.responses.MessageResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import com.example.ride_easy.exceptions.RideException;
import com.example.ride_easy.exceptions.UserException;
import com.example.ride_easy.models.Ride;
import com.example.ride_easy.repositories.RideRepository;
import com.example.ride_easy.responses.PaymentLinkResponse;
import com.example.ride_easy.services.RideService;
import com.example.ride_easy.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequestMapping("/api")
public class PaymentController {


	private UserService userService;
	private RideService rideService;
	private RideRepository rideRepository;


	public PaymentController(UserService userService,RideService rideService,RideRepository rideRepository) {

		this.userService=userService;
		this.rideService=rideService;
		this.rideRepository=rideRepository;

	}

	@PostMapping("/payments/{rideId}")
	public ResponseEntity<PaymentLinkResponse>createPaymentLink(@PathVariable Integer rideId,
																@RequestHeader("Authorization")String jwt)
					throws RazorpayException, UserException, RideException {


		Ride ride=rideService.findRideById(rideId);
		 try {
		      // Instantiate a Razorpay client with your key ID and secret
		      RazorpayClient razorpay = new RazorpayClient("rzp_test_kTsRSaDC8hwztX", "LieoD1s9mxMIv569PcgRDMcU");

		      // Create a JSON object with the payment link request parameters
		      JSONObject paymentLinkRequest = new JSONObject();
		      paymentLinkRequest.put("amount",(int) Math.round(ride.getFare())* 100);
		      paymentLinkRequest.put("currency","INR");
//		      paymentLinkRequest.put("expire_by",1691097057);
//		      paymentLinkRequest.put("reference_id",ride.getId().toString());
		      //ride.getId().toString()

		      // Create a JSON object with the customer details
		      JSONObject customer = new JSONObject();
		      customer.put("name",ride.getUser().getFullName());
		      customer.put("contact",ride.getUser().getMobile());
		      customer.put("email",ride.getUser().getEmail());
		      paymentLinkRequest.put("customer",customer);

		      // Create a JSON object with the notification settings
		      JSONObject notify = new JSONObject();
		      notify.put("sms",true);
		      notify.put("email",true);
		      paymentLinkRequest.put("notify",notify);

		      // Set the reminder settings
		      paymentLinkRequest.put("reminder_enable",true);

		      // Set the callback URL and method
		      paymentLinkRequest.put("callback_url","http://localhost:3000/ride/"+ride.getId()+"/payment/success");
		      paymentLinkRequest.put("callback_method","get");

		      // Create the payment link using the paymentLink.create() method
		      PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

		      String paymentLinkId = payment.get("id");
		      String paymentLinkUrl = payment.get("short_url");

		      PaymentLinkResponse res=new PaymentLinkResponse(paymentLinkUrl,paymentLinkId);

		      //PaymentLink fetchedPayment = razorpay.paymentLink.fetch(paymentLinkId);


		      //Print the payment link ID and URL
		      System.out.println("Payment link ID: " + res.getPaymentLinkId());
		      System.out.println("Payment link URL: " + res.getPaymentLinkUrl());

		      return new ResponseEntity<PaymentLinkResponse>(res,HttpStatus.ACCEPTED);

		    } catch (RazorpayException e) {

		      System.out.println("Error creating payment link: " + e.getMessage());
		      throw new RazorpayException(e.getMessage());
		    }


//		order_id
	}

  @GetMapping("/payments")
  public ResponseEntity<MessageResponse> redirect(@RequestParam(name="payment_id") String paymentId,
												  @RequestParam("order_id")Integer rideId) throws RazorpayException, RideException {
	  RazorpayClient razorpay = new RazorpayClient("rzp_test_kTsRSaDC8hwztX", "LieoD1s9mxMIv569PcgRDMcU");
	  Ride ride=rideService.findRideById(rideId);

	  try {


		Payment payment = razorpay.payments.fetch(paymentId);
		System.out.println("payment details --- "+payment+payment.get("status"));

		if(payment.get("status").equals("captured")) {
			System.out.println("payment details --- "+payment+payment.get("status"));

			ride.getPaymentDetails().setPaymentId(paymentId);
			ride.getPaymentDetails().setPaymentStatus(PaymentStatus.COMPLETED);

//			order.setOrderItems(order.getOrderItems());
			System.out.println(ride.getPaymentDetails().getPaymentStatus()+"payment status ");
			rideRepository.save(ride);;
		}
		MessageResponse res= new MessageResponse("your order get placed");
	      return new ResponseEntity<>(res,HttpStatus.OK);

	} catch (Exception e) {
		System.out.println("errrr payment -------- ");
		new RedirectView("http://localhost:3000/payment/failed");
		throw new RazorpayException(e.getMessage());
	}

  }

}
