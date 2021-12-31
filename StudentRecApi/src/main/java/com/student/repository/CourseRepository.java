package com.student.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.student.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
	@Query(nativeQuery = true, value = "select count(*) from Course")
	Long getTotalRecord();

	@Query(value = "select c.courseId,c.courseName, c.status, c.courseLevel, c.startDate, c.endDate,c.cohortSizeMale,c.cohortSizeFemale,c.batchNo,"
			+ "c.trainingLoaction from Course c where c.courseId  like %:courseId% or c.courseName like %:courseName% group by c.courseId  ")
	List<Course> getCourseByPager(@Param("courseId") String courseId, @Param("courseName") String courseName,
			Pageable page);
	
	@Query(value = "select c.courseId,c.courseName, c.status, c.courseLevel, c.startDate, c.endDate,c.cohortSizeMale,c.cohortSizeFemale,c.batchNo,"
			+ "c.trainingLoaction from Course c group by c.courseId  ")
	List<Course> getCourseByPagerWithoutFilter(Pageable page);
}
