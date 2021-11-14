package com.student.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.student.dto.StudentDto;
import com.student.dto.common.GenericResponse;
import com.student.service.UploadService;
import com.student.util.CSVHelper;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/upload")
public class UploadController {
	
	@Autowired
	private UploadService uploadService;
	
	@PostMapping("/upload-student")
	public GenericResponse readCSV(@RequestParam("file") MultipartFile file) {
		
		return uploadService.uploadStudentCSV(file);
	}
	
	
	
}
