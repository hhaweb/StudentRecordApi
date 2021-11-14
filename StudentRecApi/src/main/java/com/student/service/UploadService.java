package com.student.service;

import org.springframework.web.multipart.MultipartFile;

import com.student.dto.common.GenericResponse;

public interface UploadService {
	
	GenericResponse uploadStudentCSV(MultipartFile file);
	
}
