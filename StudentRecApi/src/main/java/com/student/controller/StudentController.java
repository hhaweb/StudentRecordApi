package com.student.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.student.dto.NewStudentDto;
import com.student.dto.StudentListDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.ResponseMessage;
import com.student.service.StudentService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/student")
public class StudentController {

	
	 @Autowired
	 private StudentService studentService;
	 
	 @PostMapping("/add-student")
	 private GenericResponse saveStudent(@RequestBody NewStudentDto studentDto){
        try {
        	return studentService.saveStudent(studentDto);
        }
        catch(Exception e) {
        	return  new GenericResponse(false, ResponseMessage.INTERNAL_ERROR);
        }
		 
	 }
	 
	 @PostMapping("/student-lists")
	 private GenericResponse getAllStudents(@RequestBody StudentListDto studentListDto) {
		 try {
	        	return studentService.getAllStudents(studentListDto);
	        }
	        catch(Exception ex) {
	        	return  new GenericResponse(false, ResponseMessage.INTERNAL_ERROR);
	        } 
	 }
}
