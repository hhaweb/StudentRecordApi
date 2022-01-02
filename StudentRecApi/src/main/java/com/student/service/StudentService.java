package com.student.service;

import java.util.List;

import com.student.dto.StudentDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;

public interface StudentService {
	
	StudentDto getStudentById(Long id);
	StudentDto getStudentByUserId(Long userId);
	List<StudentDto> getStudentWithPager(SearchDto searchDto);
	GenericResponse saveStudent(StudentDto studentDto);
	List<StudentDto> getStudentByCourseId(String courseId);

}
