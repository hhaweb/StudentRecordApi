package com.student.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.student.dto.CourseDto;
import com.student.dto.StudentDto;
import com.student.dto.TrainerDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;
import com.student.service.CourseService;
import com.student.service.TrainerService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/course")
public class CourseController {
	@Autowired
	CourseService courseService;
	@Autowired
	TrainerService trainerService;
	
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
	public List<CourseDto> getCourseList(@Valid @RequestBody SearchDto search) {
		
		List<CourseDto> courseDtoList = new ArrayList<CourseDto>();
		try {
			courseDtoList = courseService.getCourseListWithPager(search);
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
	
	
}
