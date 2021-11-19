package com.student.service.impl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
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

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderColumnNameMappingStrategyBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.student.config.CSVConfig;
import com.student.config.ERole;
import com.student.config.ResponseMessage;
import com.student.dto.StudentDto;
import com.student.dto.UploadResultDto;
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
		UploadFileRecord uploadFileRecord = new UploadFileRecord(fileName, "", 0, 0, 0, new Date(), loginUser.getUsername());
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
		User existingUser = userRepo.findByUserName(userName).orElse(null);
		if (existingUser == null) {
			String password = commonUtil.generatePassword();
			User user = new User(userName, encoder.encode(password), new Date(), new Date(), role);
			User saveObj = userRepo.saveAndFlush(user);
			return saveObj.getId();
		}
		return existingUser.getId();
	}

	@Override
	public GenericResponse uploadStudentCSV(MultipartFile file) {
		// TODO Auto-generated method stub
		if (CSVHelper.validateCSVFile(file, CSVConfig.StudentCSVHeader)) {
			Long uploadFileId = saveUploadFile(file.getOriginalFilename());
			Boolean checkFileSave = CSVHelper.saveFile(file, uploadFileId.toString(), uploadPath);
			if (!checkFileSave) {
				return new GenericResponse(false, ResponseMessage.FILE_SAVE_ERROR);
			}

			String path = uploadPath + File.separator + uploadFileId.toString() + ".csv";
			String errroPath = uploadPath + File.separator + uploadFileId.toString() +  CSVConfig.errorFileName;

			// read csv file
			List<StudentDto> studentCSVList = new ArrayList<StudentDto>();
			try {
				studentCSVList = new CsvToBeanBuilder<StudentDto>(new FileReader(path)).withType(StudentDto.class)
						.withIgnoreLeadingWhiteSpace(true).build().parse();
			} catch (Exception e) {
				e.printStackTrace();
			}

			List<StudentDto> errorList = new ArrayList<StudentDto>();
			int successCount = 0;
			int failCount = 0;
			Role studentRole = roleRepo.findByName(ERole.ROLE_STUDENT).orElse(null);
			Set<Role> roles = new HashSet<>();
			roles.add(studentRole);

			try {
				CSVWriter csvWriter = new CSVWriter(new FileWriter(errroPath));
				List<String> headerList = CSVConfig.StudentErrorCSVHeader;
				String[] headers = (String[]) headerList.toArray();
				for (StudentDto student : studentCSVList) {
					checkStudentCSVError(student);
					if (student.isHaveError()) {
						if (failCount == 0) {
							csvWriter.writeNext(headers);
						}
						csvWriter.writeNext(new String[] { student.getName(), student.getCid(), student.getDid(),
								student.getMobileNo(), student.getGender(), student.getPermAddress(),
								student.getAlternativeNo(), student.getTraining(), student.getErrorMessage() });
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
				csvWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		UploadFileRecord existingUploadFileRecord =	uploadFileRepo.findById(uploadFileId).orElse(null);
		if(existingUploadFileRecord != null) {
			existingUploadFileRecord.setTotalRecord(successCount + failCount);
			existingUploadFileRecord.setFailRecord(failCount);
			existingUploadFileRecord.setSuccessRecord(successCount);
			existingUploadFileRecord.setErrorFileName(uploadFileId.toString() +  CSVConfig.errorFileName);
			uploadFileRepo.save(existingUploadFileRecord);
		}
			
		return new GenericResponse(true, new UploadResultDto(0, successCount, failCount, successCount + failCount));

		} else {
			return new GenericResponse(false, ResponseMessage.INVALID_UPLOAD);
		}

	}

}
