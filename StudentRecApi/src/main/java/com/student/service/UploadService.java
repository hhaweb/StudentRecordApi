package com.student.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.student.dto.UploadHistoryDto;
import com.student.dto.common.GenericResponse;
import com.student.entity.UploadHistory;

public interface UploadService {
	UploadHistoryDto getUploadHistoryById(Long id);
	GenericResponse uploadData(MultipartFile file, String uploadType);
	List<UploadHistoryDto> getUploadHistory();

}
