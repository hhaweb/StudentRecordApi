package com.student.service;



import com.student.dto.NewStudentDto;
import com.student.dto.StudentListDto;
import com.student.dto.common.GenericResponse;

public interface StudentService {
	
	
	GenericResponse saveStudent(NewStudentDto studentDto);
	
	GenericResponse getAllStudents( StudentListDto studentListDto);
	
	

}
