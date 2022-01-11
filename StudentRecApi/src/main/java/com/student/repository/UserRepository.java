package com.student.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.student.entity.Trainer;
import com.student.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUserName(String username);
	Boolean existsByUserName(String username);
	Boolean existsByEmail(String email);
	
	@Query(value = "select u from User u where u.id =:id or u.userName like %:userName%")
	List<User> getUserByPager(@Param("id") Long id, @Param("userName") String userName,  Pageable page);
	
	@Query(nativeQuery =true,value = "select count(*) from user")
	Long getTotalRecord();
	
}
