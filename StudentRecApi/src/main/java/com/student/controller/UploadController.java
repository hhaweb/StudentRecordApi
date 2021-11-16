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
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@PostMapping("/upload-student")
	public GenericResponse readCSV(@RequestParam("file") MultipartFile file) {
		
		return uploadService.uploadStudentCSV(file);
	}
	
	@RequestMapping(path = "/download-student-error", method = RequestMethod.GET)
	public ResponseEntity<Resource> download(@RequestParam("fileId") String id) throws IOException {

	    String fileName = "error.csv";
		File file = new File(uploadPath+ File.separator + "44_err.csv");
	    Path path = Paths.get(file.getAbsolutePath());
	    ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

	    
		HttpHeaders header = new HttpHeaders();
		header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
		header.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		
	    return ResponseEntity.ok()
	    		.contentType(MediaType.parseMediaType("text/csv"))
	    		.headers(header)
	    		.body(resource);
	}
	
	
	
}
