package com.student.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.student.config.ConfigData;
import com.student.config.ERole;
import com.student.config.ResponseMessage;
import com.student.dto.CourseDto;
import com.student.dto.CourseModel;
import com.student.dto.StudentDto;
import com.student.dto.UserDetailsImpl;
import com.student.dto.UserDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;
import com.student.dto.common.SelectedItem;
import com.student.dto.csv.StudentCsvDto;
import com.student.entity.Employment;
import com.student.service.CommonService;
import com.student.service.CourseService;
import com.student.service.StudentService;
import com.student.util.CSVHelper;
import com.student.util.ExcelWriter;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/student")
public class StudentController {
	@Autowired
	StudentService studentService;
	@Autowired
	CourseService courseService;
	@Autowired
	CommonService commonService;

	@Value("${upload.path}")
	private String uploadPath;
	
	@GetMapping("/get-student-by-id")
	public StudentDto getStudentById(@RequestParam("studentId") String id) {
		Long studentId = Long.parseLong(id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		UserDto userDto = new UserDto(userDetails);
		if(userDto.getRoleName().equalsIgnoreCase(ERole.ROLE_STUDENT.toString())) {
			StudentDto studentDto = studentService.getStudentByCid(userDto.getUserName());
			if(!studentDto.getId().equals(studentId)) { // try to stop from looking other student data
				return null;
			}
		}
		StudentDto studentDto = studentService.getStudentById(studentId);
		return studentDto;

	}
	
	@GetMapping("/get-student-by-courseId")
	public List<StudentDto> getStudentByCourseId(@RequestParam("courseId") String courseId) { // id from course table not courseId
		List<StudentDto> studentDtoList = new ArrayList<StudentDto>();
		Long id = Long.parseLong(courseId);
		studentDtoList = studentService.getStudentByCourseId(id);
		return studentDtoList;

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
	
	@GetMapping("/delete-student")
	public GenericResponse deleteStudent(@RequestParam("studentId") String studentId) {
		try {
			Long id = Long.parseLong(studentId);
			return studentService.deleteStudent(id);
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse(false, ResponseMessage.SERVER_ERROR);// TODO: handle exception
		}
	}
	
	@PostMapping("/delete-students")
	public GenericResponse deleteStudents(@Valid @RequestBody List<StudentDto> studentList) {
		try {
			
			return studentService.deleteStudents(studentList);
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
	
	@GetMapping("export-student-detail")
	public void exportSaleHeaderList(HttpServletResponse response, @RequestParam("studentId") String id) {
		Long studentId = Long.parseLong(id);
		StudentDto studentDto =  studentService.getStudentById(studentId);
		List<CourseDto> courseList = courseService.getCourseByCid(studentDto.getCid());
		List<CourseModel> recommandCourseList = courseService.getRecommendedCourses(studentDto.getCid());
		studentDto.setCourseList(courseList);
		studentDto.setRecommandCourses(recommandCourseList);
		System.out.println("uploadPath ===="+uploadPath);
		ExcelWriter.exportStudentDetails(response, studentDto, uploadPath);
	}
	
	@GetMapping("student-status")
	public List<SelectedItem> getStudentStatus() {
		return commonService.getStudentStatus();
		
	}
	

}
