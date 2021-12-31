package com.student.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.student.dto.UploadFileRecordDto;
import com.student.dto.common.GenericResponse;
import com.student.entity.UploadFileRecord;

public interface UploadService {
	UploadFileRecord getUploadFileRecordById(Long id);
	
	GenericResponse uploadData(MultipartFile file, String uploadType);
	List<UploadFileRecordDto> getUploadHistory();

}
