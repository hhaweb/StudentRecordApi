package com.student.repository.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.config.ERole;
import com.student.entity.user.Role;


public interface RoleRepository extends JpaRepository<Role, Integer>{
	Optional<Role> findByName(ERole roleUser);
}
