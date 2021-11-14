package com.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.student.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

	@Query(nativeQuery =true,value = "select count(cid) from student where cid =:cid")
	int checkCidNumber(@Param("cid") String cid);
	
	@Query(nativeQuery =true,value = "select count(did) from student where did =:did")
	int checkDidNumber(@Param("did") String did);

}
