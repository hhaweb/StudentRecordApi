package com.student.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
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

import com.student.config.ConfigData;
import com.student.dto.UploadHistoryDto;
import com.student.dto.common.GenericResponse;
import com.student.entity.UploadHistory;
import com.student.service.UploadService;

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
	public ResponseEntity download(@RequestParam("fileId") String id,@RequestParam("type") String type) throws IOException {
		HttpHeaders header = new HttpHeaders();
		ByteArrayResource resource = null;
		try {
			UploadHistoryDto uploadFileRecord = uploadService.getUploadHistoryById(Long.parseLong(id));
			String fileName = type.equalsIgnoreCase("error") ? uploadFileRecord.getId()+ConfigData.errorFileName : uploadFileRecord.getId()+".csv";
			String displayFileName = type.equalsIgnoreCase("error") ? uploadFileRecord.getErrorFileName() : uploadFileRecord.getFileName();
			
			File file = new File(uploadPath + File.separator + fileName);
			Path path = Paths.get(file.getAbsolutePath());
			resource = new ByteArrayResource(Files.readAllBytes(path));

			header.set(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=" + displayFileName);
			header.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		} catch (Exception e) {
			return ResponseEntity.ok().headers(header).body(resource);
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/csv")).headers(header).body(resource);
	}

	@GetMapping("get-upload-history")
	public List<UploadHistoryDto> getUploadHistory() {
		return uploadService.getUploadHistory();

	}

}
