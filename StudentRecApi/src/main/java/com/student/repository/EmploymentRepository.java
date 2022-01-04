package com.student.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.student.entity.Employment;

public interface EmploymentRepository extends JpaRepository<Employment, Long> {
	Optional<Employment> findByCid(String cid);
}
