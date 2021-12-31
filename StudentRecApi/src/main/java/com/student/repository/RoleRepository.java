package com.student.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.config.ERole;
import com.student.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Long>{
	Optional<Role> findByName(ERole roleUser);
}
