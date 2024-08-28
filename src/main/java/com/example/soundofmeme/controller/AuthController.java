package com.example.soundofmeme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.soundofmeme.configuration.JwtProvider;
import com.example.soundofmeme.entity.User;
import com.example.soundofmeme.repository.UserRepository;
import com.example.soundofmeme.request.LoginRequest;
import com.example.soundofmeme.response.AuthenticationResponse;
import com.example.soundofmeme.service.CustomUserServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomUserServiceImpl customUserServiceImplementation;

	@PostMapping("/userSignup")
	public ResponseEntity<AuthenticationResponse> createUserHandler(@RequestBody User user) {

		String email = user.getEmail();
		String password = user.getPassword();
		String Name = user.getName();

		User isEmailExist = userRepo.findByEmail(user.getEmail());

		AuthenticationResponse authResponse = new AuthenticationResponse();

		if (isEmailExist != null) {
			authResponse.setMessage("Email is Already Used with another account");
			authResponse.setSuccess(false);
			return new ResponseEntity<>(authResponse, HttpStatus.BAD_REQUEST);
		}
		User addUser = new User();
		addUser.setEmail(email);
		addUser.setPassword(passwordEncoder.encode(password));
		addUser.setName(Name);
		User savedUser = userRepo.save(addUser);

		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),
				savedUser.getPassword());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtProvider.generateToken(authentication);

		// AuthResponse authResponse = new AuthResponse();

		authResponse.setJwt(token);
		authResponse.setMessage("Account Created Successfully");
		authResponse.setSuccess(true);
		return new ResponseEntity<AuthenticationResponse>(authResponse, HttpStatus.CREATED);

	}

	@PostMapping("/userSignin")
	public ResponseEntity<AuthenticationResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) {

		String username = loginRequest.getEmail();

		String password = loginRequest.getPassword();

		Authentication authentication = authenticate(username, password);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtProvider.generateToken(authentication);

		User userAgent = userRepo.findByEmail(username);

		AuthenticationResponse authResponse = new AuthenticationResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Login Successfullyy");
		authResponse.setUser(userAgent);
		authResponse.setSuccess(true);

		return new ResponseEntity<AuthenticationResponse>(authResponse, HttpStatus.CREATED);
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userAgentDetails = customUserServiceImplementation.loadUserByUsername(username);

		if (userAgentDetails == null) {
			throw new BadCredentialsException("Invalid Email Id...");
		}

		if (!passwordEncoder.matches(password, userAgentDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Password for email id" + userAgentDetails.getUsername());
		}

		System.out.println(userAgentDetails);
		return new UsernamePasswordAuthenticationToken(userAgentDetails, null, userAgentDetails.getAuthorities());
	}
	


}
