package com.student.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.student.dto.user.UserDetailsImpl;
import com.student.entity.user.User;
import com.student.repository.user.UserRepository;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepo;
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
		return UserDetailsImpl.build(user);
	}
	
	

}