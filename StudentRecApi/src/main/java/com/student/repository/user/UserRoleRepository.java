package com.student.repository.user;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.entity.user.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer>{
	@Transactional
	Long deleteByUserId(Long userId);
}
