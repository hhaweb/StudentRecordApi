package com.student.repository;


import java.security.cert.PKIXRevocationChecker.Option;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.student.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {

	@Query(value = "select case when count(s)> 0 then true else false end from Student s where s.cid =:cid")
	boolean isExistCidNumber(@Param("cid") String cid);
	
	@Query(value = "select case when count(s)> 0 then true else false end from Student s where s.cid =:cid and s.id !=:id")
	boolean isExistCidNumberById(@Param("cid") String cid, @Param("id") Long id);
	
	@Query(value = "select case when count(s)> 0 then true else false end from Student s where s.did =:did")
	boolean isExistDidNumber(@Param("did") String did);
	
	@Query(value = "select case when count(s)> 0 then true else false end  from Student s where s.did =:did and s.id !=:id")
	boolean isExistDidNumberById(@Param("did") String did, @Param("id") Long id);
	
	@Query(value = "select case when count(s)> 0 then true else false end from Student s where s.id=:id")
	boolean isExistStudent(@Param("id") Long id);
	
	@Query(nativeQuery =true,value = "select count(*) from student")
	Long getTotalRecord();
	
	Optional<Student> findByUserId(Long userId);

}
