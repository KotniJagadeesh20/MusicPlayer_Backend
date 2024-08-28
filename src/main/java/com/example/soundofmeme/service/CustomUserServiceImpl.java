package com.example.soundofmeme.service;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.soundofmeme.entity.User;
import com.example.soundofmeme.repository.UserRepository;



@Service
public class CustomUserServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	

	public CustomUserServiceImpl(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userAgentAdd = userRepo.findByEmail(username);

		if (userAgentAdd == null) {
			throw new UsernameNotFoundException("User or Agent Not Found with the Email:" + username);
		}
		List<GrantedAuthority> authorities = new LinkedList<>();
		
		return new org.springframework.security.core.userdetails.User(userAgentAdd.getEmail(),
				userAgentAdd.getPassword(), authorities);
	}

}
