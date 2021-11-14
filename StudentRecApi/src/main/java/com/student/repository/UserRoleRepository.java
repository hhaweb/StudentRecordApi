package com.student.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer>{
	@Transactional
	Long deleteByUserId(Long userId);
}
