package com.student.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.student.dto.UserDetailsImpl;
import com.student.entity.User;
import com.student.repository.UserRepository;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUserName(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with user name: " + username));
		return UserDetailsImpl.build(user);
	}


}
