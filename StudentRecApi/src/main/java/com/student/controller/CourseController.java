package com.student.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.student.config.ResponseMessage;
import com.student.dto.CourseDto;
import com.student.dto.CourseModel;
import com.student.dto.StudentDto;
import com.student.dto.TrainerDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;
import com.student.dto.common.SelectedItem;
import com.student.dto.csv.CourseCsvDto;
import com.student.dto.csv.StudentCsvDto;
import com.student.dto.csv.TrainerCsvDto;
import com.student.entity.Role;
import com.student.service.CommonService;
import com.student.service.CourseService;
import com.student.service.StudentService;
import com.student.service.TrainerService;
import com.student.util.CSVHelper;
import com.student.util.ExcelWriter;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/course")
public class CourseController {
	@Autowired
	CourseService courseService;
	@Autowired
	TrainerService trainerService;
	@Autowired
	StudentService studentService;
	@Autowired
	CommonService commonService;
	
	@Value("${upload.path}")
	private String uploadPath;
	@GetMapping("/get-course-by-id")
	public CourseDto getCourseById(@RequestParam("courseId") String id) {
		Long courseId = Long.parseLong(id);
		CourseDto courseDto = null;
		try {
			courseDto = courseService.getCourseById(courseId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return courseDto;
	}
	
	@PostMapping("/save-course")
	public GenericResponse saveCourse(@Valid @RequestBody CourseDto courseDto) {
		try {
			return courseService.saveCourse(courseDto);
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse(false, ResponseMessage.SERVER_ERROR);// TODO: handle exception
		}
	}
	
	@PostMapping("/get-course-list")
	public List<CourseModel> getCourseList(@Valid @RequestBody SearchDto search) {
		
		List<CourseModel> courseDtoList = new ArrayList<CourseModel>();
		try {
			courseDtoList = courseService.getCourseListWithPager(search);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return courseDtoList;
	}
	
	@GetMapping("/get-courses-by-cid")
	public List<CourseDto> getCoursesBycId(@RequestParam("cId") String cId) {
		
		List<CourseDto> courseDtoList = new ArrayList<CourseDto>();
		try {
			courseDtoList = courseService.getCourseByCid(cId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return courseDtoList;
	}
	
	@PostMapping("/get-trainer-list")
	public List<TrainerDto> getTrainerList(@Valid @RequestBody SearchDto search) {
		
		List<TrainerDto> trainerDtoList = new ArrayList<TrainerDto>();
		try {
			trainerDtoList = trainerService.getTrainerListWithPager(search);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return trainerDtoList;
	}
	
	@GetMapping("/get-trainer-by-id")
	public TrainerDto getTrainerById(@RequestParam("trainerId") String id) {
		TrainerDto trainerDto = null;
		try {
			Long trainerId = Long.parseLong(id);
			trainerDto = trainerService.getTrainerById(trainerId);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return trainerDto;
		
	}
	
	@PostMapping("/save-trainer")
	public GenericResponse saveTrainer(@Valid @RequestBody TrainerDto trainerDto) {
		GenericResponse response = new GenericResponse();
		try {
			response = trainerService.saveTrainer(trainerDto);
		}catch (Exception e) {
			e.printStackTrace();
			response.setMessage(ResponseMessage.SERVER_ERROR);
		}
		return response;
		
	}
	
	@GetMapping("/delete-trainer")
	public GenericResponse deleteTrainer(@RequestParam("trainerId") String trainerId) {
		GenericResponse response = new GenericResponse();
		try {
			Long id = Long.parseLong(trainerId);
			response = trainerService.deleteTrainer(id);
		}catch (Exception e) {
			e.printStackTrace();
			response.setMessage(ResponseMessage.SERVER_ERROR);
		}
		return response;
		
	}
	
	@GetMapping("/delete-course")
	public GenericResponse deleteCourse(@RequestParam("courseId") String courseId) {// id from course table not courseId
		GenericResponse response = new GenericResponse();
		try {
			Long id = Long.parseLong(courseId);
			
			response = courseService.deleteCourse(id);
		}catch (Exception e) {
			e.printStackTrace();
			response.setMessage(ResponseMessage.SERVER_ERROR);
		}
		return response;
		
	}
	
	@GetMapping("/get-recommend-courses")
	public List<CourseModel> getRecommendedCourses(@RequestParam("cid") String cid) {
		
		List<CourseModel> courseDtoList = new ArrayList<CourseModel>();
		try {
			courseDtoList = courseService.getRecommendedCourses(cid);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return courseDtoList;
	}
	
	@GetMapping("export-course-detail")
	public void exportSaleHeaderList(HttpServletResponse response, @RequestParam("courseId") String id) { // id from course table not courseId
		Long courseId = Long.parseLong(id);
		CourseDto courseDto = courseService.getCourseById(courseId);
		List<StudentDto> studentList = studentService.getStudentByCourseId(courseDto.getCourseId());
		courseDto.setStudentList(studentList);	
		ExcelWriter.exportCourseDetail(response, courseDto, uploadPath);
	}
	
	@PostMapping("export-course-list")
	public void exportCourseList(HttpServletResponse response, @RequestBody SearchDto searchDto) {
		searchDto.isExport = true;
		
		List<CourseModel> courseDtoList = new ArrayList<CourseModel>();
		List<CourseCsvDto> courseCsvList = new ArrayList<CourseCsvDto>();
		try {
			courseDtoList = courseService.getCourseListWithPager(searchDto);
			for (CourseModel courseModel : courseDtoList) {
				CourseCsvDto courseCSV = new CourseCsvDto(courseModel);
				courseCsvList.add(courseCSV);
			}
			
			CSVHelper.exportCourseList(response, courseCsvList);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	} 
	
	@PostMapping("export-trainer-list")
	public void exportTrainerList(HttpServletResponse response, @RequestBody SearchDto searchDto) {
		searchDto.isExport = true;
		List<TrainerDto> trainerList = trainerService.getTrainerListWithPager(searchDto);
		List<TrainerCsvDto> trainerCsvDtoList = new ArrayList<TrainerCsvDto>();
		for (TrainerDto trainer : trainerList) {
			TrainerCsvDto studentCsvDto = new TrainerCsvDto(trainer);
			trainerCsvDtoList.add(studentCsvDto);
		}
			CSVHelper.exportTrainerList(response, trainerCsvDtoList);	
	} 
	
	@GetMapping("get-trainer-item")
	public List<SelectedItem> getTrainerItem() {	
		return trainerService.getAllTrainer();
	}
	
	@GetMapping("course-level")
	public List<SelectedItem> getStudentStatus() {
		return commonService.getCourseLevel();
		
	}
	
	
}
