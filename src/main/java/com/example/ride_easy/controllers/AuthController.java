package com.example.ride_easy.controllers;


import com.example.ride_easy.configs.JwtUtil;
import com.example.ride_easy.enums.UserRole;
import com.example.ride_easy.exceptions.UserException;
import com.example.ride_easy.models.User;
import com.example.ride_easy.models.Driver;
import com.example.ride_easy.repositories.DriverRepository;
import com.example.ride_easy.repositories.UserRepository;
import com.example.ride_easy.requests.DriversSignupRequest;
import com.example.ride_easy.requests.LoginRequest;
import com.example.ride_easy.requests.SignupRequest;
import com.example.ride_easy.responses.JwtResponce;
import com.example.ride_easy.services.CustomUserDetailsService;
import com.example.ride_easy.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private DriverRepository driverRepository;
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	
	@PostMapping("/user/signup")
	public ResponseEntity<JwtResponce> signupHandler(@RequestBody SignupRequest signupRequest)throws UserException, AuthenticationException{

		
        User user = userRepository.findByEmail(signupRequest.getEmail());
        
        JwtResponce jwtResponse=new JwtResponce();
        
		
		if(user!=null) {
			throw new UserException("User Already Exist With Email "+signupRequest.getEmail());
		}
        
		String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        // Create new user account
        User createdUser = new User();
        createdUser.setEmail(signupRequest.getEmail());
        createdUser.setPassword(encodedPassword);
        createdUser.setFullName(signupRequest.getFullName());
        createdUser.setMobile(signupRequest.getMobile());
        createdUser.setRole(UserRole.USER);
        
        User savedUser=userRepository.save(createdUser);
        
        Authentication authentication=new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Generate JWT token
        String jwt = jwtUtil.generateJwtToken(authentication);
        
        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setType(UserRole.USER);
        jwtResponse.setMessage("Account Created Successfully: "+savedUser.getFullName());
        
        return new ResponseEntity<JwtResponce>(jwtResponse,
        		HttpStatus.ACCEPTED);
		
	}
	
	@PostMapping("/driver/signup")
	public ResponseEntity<JwtResponce> driverSignupHandler(@RequestBody DriversSignupRequest driverSignupRequest){
		
		Driver driver = driverRepository.findByEmail(driverSignupRequest.getEmail());
		
		JwtResponce jwtResponse=new JwtResponce();
			
		if(driver!=null) {
			
			jwtResponse.setAuthenticated(false);
		    jwtResponse.setErrorDetails("email already used with another account");
		    jwtResponse.setError(true);
		        
			return new ResponseEntity<JwtResponce>(jwtResponse,HttpStatus.BAD_REQUEST);
		}
		
		
		
		
		Driver createdDriver=driverService.registerDriver(driverSignupRequest);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(createdDriver.getEmail(), createdDriver.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
		String jwt = jwtUtil.generateJwtToken(authentication);
        
        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setType(UserRole.DRIVER);
        jwtResponse.setMessage("Account Created Successfully: "+createdDriver.getName());
        
        
		return new ResponseEntity<JwtResponce>(jwtResponse,HttpStatus.ACCEPTED);
	}
	
	
	@PostMapping("/signin")
    public ResponseEntity<JwtResponce> signin(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        
        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = jwtUtil.generateJwtToken(authentication);
        
        JwtResponce jwtResponse=new JwtResponce();
        
        jwtResponse.setJwt(jwt);
        jwtResponse.setAuthenticated(true);
        jwtResponse.setError(false);
        jwtResponse.setErrorDetails(null);
        jwtResponse.setMessage("Account loggedIn Successfully: ");

        return new ResponseEntity<JwtResponce>(jwtResponse,HttpStatus.OK);
    }
	
	private Authentication authenticate(String username, String password) {
		System.out.println("sign in userDetails - "+password);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        System.out.println("sign in userDetails after loaduser- "+userDetails);
        
        if (userDetails == null) {
        	System.out.println("sign in userDetails - null " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
        	System.out.println("sign in userDetails - password not match " + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
