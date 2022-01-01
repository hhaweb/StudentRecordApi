package com.student.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.student.config.ResponseMessage;
import com.student.dto.StudentDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;
import com.student.dto.csv.StudentCsvDto;
import com.student.service.StudentService;
import com.student.util.CSVHelper;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/student")
public class StudentController {
	@Autowired
	StudentService studentService;

	@GetMapping("/get-student-by-id")
	public StudentDto getStudentById(@RequestParam("studentId") String id) {
		Long studentId = Long.parseLong(id);
		StudentDto studentDto = studentService.getStudentById(studentId);
		return studentDto;

	}

	@PostMapping("/get-student-list")
	public List<StudentDto> getStudentListWithPager(@Valid @RequestBody SearchDto search) {
		return studentService.getStudentWithPager(search);
	}

	@PostMapping("/save-student")
	public GenericResponse save(@Valid @RequestBody StudentDto studentDto) {
		try {
			return studentService.saveStudent(studentDto);
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse(false, ResponseMessage.SERVER_ERROR);// TODO: handle exception
		}
	}
	
	@PostMapping("export-student-list")
	public void exportSaleHeaderList(HttpServletResponse response, @RequestBody SearchDto searchDto) {
		searchDto.isExport = true;
		List<StudentDto> studentDtoList = studentService.getStudentWithPager(searchDto);
		List<StudentCsvDto> studentCsvLisr = new ArrayList<StudentCsvDto>();
		for (StudentDto studentDto : studentDtoList) {
			StudentCsvDto studentCsvDto = new StudentCsvDto(studentDto);
			studentCsvLisr.add(studentCsvDto);
		}
		CSVHelper.exportStudentList(response, studentCsvLisr);
	} 

}
