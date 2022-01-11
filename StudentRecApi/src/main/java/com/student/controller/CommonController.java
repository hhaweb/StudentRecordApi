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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.student.dto.StudentDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;
import com.student.dto.common.SelectedItem;
import com.student.dto.csv.StudentCsvDto;
import com.student.entity.DropDownItem;
import com.student.service.CommonService;
import com.student.util.CSVHelper;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/common")
public class CommonController {
	@Autowired
	CommonService commonService;

	@Value("${upload.path}")
	private String uploadPath;
	
	@GetMapping("get-dropdown-item")
	List<SelectedItem> getDropDownItemByName(@RequestParam("name") String name) {
		return commonService.getDropDownItem(name);

	}

	@PostMapping("save-dropdown-item")
	GenericResponse saveDropDownItem(@RequestBody List<DropDownItem> dropDownItem) {
		GenericResponse res = new GenericResponse();
		try {
			res = commonService.saveDropDown(dropDownItem);
		} catch (Exception e) {
			res.setStatus(false);
		}
		return res;
	}

	@GetMapping("delete-dropdown-item")
	GenericResponse deleteDropDown(@RequestParam("name") String name) {
		GenericResponse res = new GenericResponse();
		try {
			res = commonService.deleteDropDown(name);
		} catch (Exception e) {
			res.setStatus(false);
		}
		return res;
	}

	@GetMapping("get-dropdown-name")
	List<SelectedItem> getDropDownName() {
		return commonService.getDropDownNames();

	}


	@GetMapping("download-upload-simple")
	public ResponseEntity<Resource> download(@RequestParam("name") String name) throws IOException {
		File file = new File(uploadPath +File.separator+"template"+File.separator+ name + ".csv");
		Path path = Paths.get(file.getAbsolutePath());
		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
		
		HttpHeaders headers = new HttpHeaders();	
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name + ".csv");
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);	
		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

}
