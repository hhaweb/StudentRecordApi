package com.student.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.student.dto.CourseModel;
import com.student.entity.Course;
import com.student.entity.Student;

public interface CourseRepository extends JpaRepository<Course, Long> {
	@Query(nativeQuery = true, value = "SELECT getCourseTotalRecords(?1, ?2)")
	Long getTotalRecordGroupBy(String courseId, String courseName);

	@Query(value = "SELECT new com.student.dto.CourseModel(c.id,c.courseId,c.courseName,c.status,c.courseLevel,c.startDate,c.endDate,c.cohortSizeMale,c.cohortSizeFemale,c.batchNo,c.trainingLoaction)  FROM Course AS c"
			+ " where c.courseId  like %:courseId% or c.courseName like %:courseName% GROUP BY c.courseId,c.courseName,c.batchNo,c.trainingLoaction")
	List<CourseModel> getCourseByPager(@Param("courseId") String courseId, @Param("courseName") String courseName,
			Pageable page);

	@Query(value = "SELECT new com.student.dto.CourseModel(c.id,c.courseId,c.courseName,c.status,c.courseLevel,c.startDate,c.endDate,c.cohortSizeMale,c.cohortSizeFemale,c.batchNo,c.trainingLoaction)  FROM Course AS c "
			+ " GROUP BY c.courseId,c.courseName,c.batchNo,c.trainingLoaction")
	List<CourseModel> getCourseByPagerWithoutFilter(Pageable page);

	List<Course> findBycId(String cId);

	@Query(value = "SELECT new com.student.dto.CourseModel(c.courseId,c.courseName,c.courseLevel,c.startDate,c.endDate,c.duration)  FROM Course AS c  where c.courseId not in (:courseIds) "
			+ "and c.sector in(:sectors) and c.status <> 'Completed' " + " GROUP BY c.courseId,c.sector")
	List<CourseModel> getRecommendCourses(@Param("courseIds") List<String> courseIds,
			@Param("sectors") List<String> sectors);

	@Transactional
	Long deleteBycId(String cid);

	@Transactional
	@Modifying
	@Query("update Course c set c.trainerId= null where c.trainerId=:trainerId ")
	void updateTrainerIdFromCourse(@Param("trainerId") String trainerId);
	
	
	@Transactional
	@Modifying
	@Query(value = "delete from Course c where c.courseId=:#{#course.courseId} "
			+ "and c.courseName=:#{#course.courseName} and c.batchNo=:#{#course.batchNo} "
			+ "and c.trainingLoaction=:#{#course.trainingLoaction} ")
	void  deleteCourseByCourseId(@Param("course")Course course);
	
	List<Course> findByCourseIdAndCourseNameAndBatchNoAndTrainingLoaction(String courseId, String courseName, String batchNo, String trainingLoaction);
}
