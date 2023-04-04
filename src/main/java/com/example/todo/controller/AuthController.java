package com.example.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.component.CustomUserDetailsService;
import com.example.todo.component.JwtResponse;
import com.example.todo.component.JwtUtil;
import com.example.todo.entities.User;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/generatetoken")
	public ResponseEntity<?> createToken(@RequestBody User user) throws Exception {
		System.out.println(user);
		try {
			System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
			authenticate(user.getUsername(), user.getPassword());
		} catch (UsernameNotFoundException e) {
			throw new Exception("bad credential");
		} catch (BadCredentialsException e) {
			throw new Exception("bad credential");
		}

		final UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(user.getUsername());

		final String token = this.jwtUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("user blocked", e);
		} catch (BadCredentialsException e) {
			throw new Exception("invalid credentials", e);
		}

	}
}
