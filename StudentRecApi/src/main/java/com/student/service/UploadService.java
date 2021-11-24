package com.student.service;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.student.dto.common.GenericResponse;
import com.student.entity.Role;

public interface UploadService {
	
	GenericResponse uploadStudentCSV(MultipartFile file);
	
	Long createUser(String userName, Set<Role> role);
	
}
