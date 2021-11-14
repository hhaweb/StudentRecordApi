package com.student.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.student.config.CSVConfig;
import com.student.config.ERole;
import com.student.config.ResponseMessage;
import com.student.dto.StudentDto;
import com.student.dto.UserDetailsImpl;
import com.student.dto.common.GenericResponse;
import com.student.entity.Role;
import com.student.entity.Student;
import com.student.entity.UploadFileRecord;
import com.student.entity.User;
import com.student.repository.RoleRepository;
import com.student.repository.StudentRepository;
import com.student.repository.UploadFileRepository;
import com.student.repository.UserRepository;
import com.student.service.UploadService;
import com.student.util.CSVHelper;
import com.student.util.CommonUtil;

@Service
public class UploadServiceImpl implements UploadService {

	@Autowired
	private UploadFileRepository uploadFileRepo;

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private CommonUtil commonUtil;

	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private UserRepository userRepo;

	@Value("${upload.path}")
	private String uploadPath;

	private Long saveUploadFile(String fileName) {
		UserDetailsImpl loginUser = commonUtil.getCurrentLoginUser();
		UploadFileRecord uploadFileRecord = new UploadFileRecord(fileName, new Date(), loginUser.getUsername());
		UploadFileRecord saveObj = uploadFileRepo.saveAndFlush(uploadFileRecord);
		return saveObj.getId();
	}

	private void checkStudentCSVError(StudentDto student) {
		String errorMessage = "";
		if (student.getCid() == null) {
			errorMessage += "cid is empty";
		}

		if (studentRepo.checkCidNumber(student.getCid()) > 0) {
			errorMessage += ", cid is already exist";
		}

		if (student.getDid() == null) {
			errorMessage += ", did is empty";
		}

		if (studentRepo.checkDidNumber(student.getDid()) > 0) {
			errorMessage += ", did is already exist";
		}
		student.setErrorMessage(errorMessage);
		student.setHaveError(errorMessage == "" ? false : true);
	}

	private Long createUser(String userName, Set<Role> role) {
		String password = commonUtil.generatePassword();
		User user = new User(userName, encoder.encode(password), new Date(), new Date(), role);
		User saveObj = userRepo.saveAndFlush(user);
		return saveObj.getId();

	}

	@Override
	public GenericResponse uploadStudentCSV(MultipartFile file) {
		// TODO Auto-generated method stub
		if (CSVHelper.validateCSVFile(file, CSVConfig.StudentCSVHeader)) {
			Long id = saveUploadFile(file.getOriginalFilename());
			Boolean checkFileSave = CSVHelper.saveFile(file, id.toString(), uploadPath);
			if (!checkFileSave) {
				return new GenericResponse(false, ResponseMessage.FILE_SAVE_ERROR);
			}

			List<StudentDto> studentCSVList = CSVHelper
					.readStudentCSV(uploadPath + File.separator + id.toString() + ".csv");
			List<StudentDto> errorList = new ArrayList<StudentDto>();
			int successCount = 0;
			int failCount = 0;
			Role studentRole = roleRepo.findByName(ERole.ROLE_STUDENT).orElse(null);
			Set<Role> roles = new HashSet<>();
			roles.add(studentRole);
			for (StudentDto student : studentCSVList) {
				checkStudentCSVError(student);
				if (student.isHaveError()) {
					errorList.add(student);
					failCount++;
				} else {
					Student saveObj = student.getEntity();
					Long userId = createUser(saveObj.getName() + "-" + saveObj.getCid(), roles);
					saveObj.setUserId(userId);
					studentRepo.save(saveObj);
					successCount++;
				}

			}
			String message = "";
			if(successCount > 0) {
				message += successCount + " record uploaded successfully. ";
			}
			
			if(failCount > 0) {
				message += failCount + " record have error";
			}
			
			return new GenericResponse(true, message, errorList);

		} else {
			return new GenericResponse(false, ResponseMessage.INVALID_UPLOAD);
		}

	}

}
