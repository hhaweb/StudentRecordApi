package com.student.service;

import java.util.List;

import com.student.dto.CourseDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;

public interface CourseService {
	CourseDto getCourseById(Long id);
	GenericResponse saveCourse(CourseDto courseDto);
	List<CourseDto> getCourseListWithPager(SearchDto searchDto);
}
