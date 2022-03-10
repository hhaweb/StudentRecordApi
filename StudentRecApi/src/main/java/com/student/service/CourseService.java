package com.student.service;

import java.util.List;

import com.student.dto.CourseDto;
import com.student.dto.CourseModel;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;

public interface CourseService {
	CourseDto getCourseById(Long id);
	GenericResponse saveCourse(CourseDto courseDto);
	GenericResponse saveCourseList(List<CourseDto> courseDtoList);
	GenericResponse removeStudent(CourseDto courseDto);
	GenericResponse deleteCourse(Long id);
	GenericResponse deleteCourses(List<CourseDto> courseDtoList);
	List<CourseModel> getCourseListWithPager(SearchDto searchDto);
	List<CourseDto> getCourseByCid(String cId);
	List<CourseModel> getRecommendedCourses(String cid);
	
}
