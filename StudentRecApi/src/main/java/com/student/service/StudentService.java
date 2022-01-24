package com.student.service;

import java.util.List;

import com.student.dto.StudentDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;
import com.student.entity.Employment;

public interface StudentService {
	
	StudentDto getStudentById(Long id);
	StudentDto getStudentByCid(String cid);
	List<StudentDto> getStudentWithPager(SearchDto searchDto);
	GenericResponse saveStudent(StudentDto studentDto);
	GenericResponse deleteStudent(Long studentId);
	List<StudentDto> getStudentByCourseId(Long id);

}
