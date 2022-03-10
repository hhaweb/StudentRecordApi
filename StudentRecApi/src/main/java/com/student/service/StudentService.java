package com.student.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
	GenericResponse deleteStudents(List<StudentDto> studentDtoList);
	List<StudentDto> getStudentByCourseId(Long id);
}
