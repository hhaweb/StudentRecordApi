package com.student.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.student.entity.user.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUserName(String username);
	Optional<User> findByEmail(String email);
	Boolean existsByUserName(String username);
	Boolean existsByEmail(String email);
	
}
