package com.student.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.student.config.ConfigData;
import com.student.dto.UploadFileRecordDto;
import com.student.dto.UploadResultDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.MenuConfigData;
import com.student.dto.csv.StudentCsvDto;
import com.student.entity.UploadFileRecord;
import com.student.service.UploadService;
import com.student.util.CSVHelper;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/upload")
public class UploadController {

	@Autowired
	private UploadService uploadService;

	@Value("${upload.path}")
	private String uploadPath;

	@PostMapping("/upload-data")
	public GenericResponse uploadStudent(@RequestParam("uploadType") String uploadType,
			@RequestParam("file") MultipartFile file) {
		GenericResponse response = uploadService.uploadData(file, uploadType);
		return response;
	}

	@RequestMapping(path = "/download-uploadFile", method = RequestMethod.GET)
	public ResponseEntity download(@RequestParam("fileId") String id) throws IOException {
		HttpHeaders header = new HttpHeaders();
		ByteArrayResource resource = null;
		try {
			UploadFileRecord uploadFileRecord = uploadService.getUploadFileRecordById(Long.parseLong(id));

			File file = new File(uploadPath + File.separator + uploadFileRecord.getId() + ".csv");
			Path path = Paths.get(file.getAbsolutePath());
			resource = new ByteArrayResource(Files.readAllBytes(path));

			header.set(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=" + uploadFileRecord.getFileName() + ConfigData.errorFileName);
			header.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		} catch (Exception e) {
			return ResponseEntity.ok().headers(header).body(resource);
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/csv")).headers(header).body(resource);
	}

	@GetMapping("get-upload-history")
	public List<UploadFileRecordDto> getUploadHistory() {
		return uploadService.getUploadHistory();

	}

}
