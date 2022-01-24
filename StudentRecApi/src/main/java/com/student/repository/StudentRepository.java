package com.student.repository;

import java.security.cert.PKIXRevocationChecker.Option;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.student.entity.Student;
import com.student.entity.Trainer;

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

	@Query(nativeQuery = true, value = "select count(*) from student")
	Long getTotalRecord();

	@Query(value = "select count(s) from Student s  where s.id =:id or s.name like %:name% or s.cid like %:name%")
	Long getTotalRecordWithFilter(@Param("id") Long id, @Param("name") String studentName);

	Optional<Student> findByUserId(Long userId);

	@Query(value = "select s from Student s where s.id =:id or s.name like %:name% or s.cid like %:name%")
	List<Student> getStudentByPager(@Param("id") Long id, @Param("name") String studentName, Pageable page);

	@Query(value = "select s from Student s where s.cid in (select c.cId from Course c where c.courseId=:courseId "
			+ "and c.courseName=:courseName and c.batchNo=:batchNo "
			+ "and c.trainingLoaction=:trainingLoaction ) ")
	List<Student> getStudentByCourseId(@Param("courseId") String courseId, @Param("courseName") String courseName,
			@Param("batchNo") String batchNo, @Param("trainingLoaction") String trainingLoaction);

	@Query(value = "select max(s.id) from Student s")
	Optional<Long> getStudentMaxId();

	Optional<Student> findByCid(String cid);
	
	List<Student> findByCidIn(Collection<String> cidList);
}
