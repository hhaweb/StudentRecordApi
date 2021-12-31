package com.student.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
import com.student.config.ConfigData;
import com.student.config.ERole;
import com.student.config.ResponseMessage;
import com.student.dto.UploadFileRecordDto;
import com.student.dto.UploadResultDto;
import com.student.dto.UserDetailsImpl;
import com.student.dto.common.GenericResponse;
import com.student.dto.csv.CourseCsvDto;
import com.student.dto.csv.StudentCsvDto;
import com.student.dto.csv.TrainerCsvDto;
import com.student.entity.Course;
import com.student.entity.Role;
import com.student.entity.Student;
import com.student.entity.Trainer;
import com.student.entity.UploadFileRecord;
import com.student.entity.User;
import com.student.repository.CourseRepository;
import com.student.repository.RoleRepository;
import com.student.repository.StudentRepository;
import com.student.repository.TrainerRepository;
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

	@Autowired
	private CourseRepository courseRepo;

	@Autowired
	private TrainerRepository trainerRepo;

	@Value("${upload.path}")
	private String uploadPath;

	/* Student Upload */
	private Long saveUploadFile(String fileName, String type) {
		UserDetailsImpl loginUser = commonUtil.getCurrentLoginUser();
		UploadFileRecord uploadFileRecord = new UploadFileRecord(fileName, fileName+ConfigData.errorFileName, 0, 0, 0, new Date(),
				loginUser.getUsername(), type);
		UploadFileRecord saveObj = uploadFileRepo.saveAndFlush(uploadFileRecord);
		return saveObj.getId();
	}

	private void checkStudentCSVError(StudentCsvDto student) {
		String errorMessage = "";

		// for empty check
		if (student.getCid() == null) {
			errorMessage += "cid is empty";
		}

		if (student.getDid() == null) {
			errorMessage += ", did is empty";
		}

		// valid date format
		if (!CSVHelper.validateDateFormat(student.getDateOfBirth(), ConfigData.DateFormat)
				&& !student.getDateOfBirth().equalsIgnoreCase("NULL")) {
			errorMessage += ", invalid date of birth  format";
		}

		if (!CSVHelper.validateDateFormat(student.getCreatedAt(), ConfigData.DateFormatWithTime)
				&& !student.getCreatedAt().equalsIgnoreCase("NULL")) {
			errorMessage += ", invalid created at date format";
		}

		if (!CSVHelper.validateDateFormat(student.getUpdatedAt(), ConfigData.DateFormatWithTime)
				&& !student.getUpdatedAt().equalsIgnoreCase("NULL")) {
			errorMessage += ", invalid created at date format";
		}

		if (!CSVHelper.validateDateFormat(student.getDeletedAt(), ConfigData.DateFormatWithTime)
				&& !student.getDeletedAt().equalsIgnoreCase("NULL")) {
			errorMessage += ", invalid deleted at date format";
		}

		// for already exit check
		if (CSVHelper.isNumeric(student.getId())) { // for update check
			if (studentRepo.isExistStudent(Long.parseLong(student.getId()))) {

				errorMessage += ", id does not exist";
			}
			if (studentRepo.isExistCidNumberById(student.getCid(), Long.parseLong(student.getId()))) {
				errorMessage += ", cid is already exist";
			}

			if (studentRepo.isExistDidNumberById(student.getDid(), Long.parseLong(student.getId()))) {
				errorMessage += ", did is already exist";
			}
		} else { // for insert check
			if (studentRepo.isExistCidNumber(student.getCid())) {
				errorMessage += ", cid is already exist";
			}

			if (studentRepo.isExistDidNumber(student.getDid())) {
				errorMessage += ", did is already exist";
			}
		}

		student.setErrorMessage(errorMessage);
		student.setHaveError(errorMessage == "" ? false : true);
	}

	private List<StudentCsvDto> readStudentCSV(String path) throws IllegalStateException, FileNotFoundException { // read
																													// csv
		List<StudentCsvDto> studentCSVList = new ArrayList<StudentCsvDto>();
		studentCSVList = new CsvToBeanBuilder<StudentCsvDto>(new FileReader(path)).withType(StudentCsvDto.class)
				.withIgnoreLeadingWhiteSpace(true).build().parse();
		return studentCSVList;
	}

	private void writeStudentCSVFile(String filePath, List<StudentCsvDto> errorList) {
		List<String> headerList = ConfigData.StudentErrorCSVHeader;
		CSVWriter csvWriter;
		try {
			csvWriter = new CSVWriter(new FileWriter(filePath));
			String[] headers = (String[]) headerList.toArray();
			csvWriter.writeNext(headers); // write header
			for (StudentCsvDto student : errorList) {
				csvWriter.writeNext(new String[] { student.getId(), student.getName(), student.getCid(),
						student.getDid(), student.getDateOfBirth(), student.getEmail(), student.getMobileNo(),
						student.getGender(), student.getBloodGroup(), student.getMartialStatus(),
						student.getEmploymentTypeId(), student.getAvatar(), student.getStatus(), student.getUserId(),
						student.getTrainingCenterId(), student.getCreatedAt(), student.getUpdatedAt(),
						student.getDeletedAt(), student.getBatchNo(), student.getTrainingYear(),
						student.getErrorMessage() });
			}
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/* Course Upload */
	private void checkCourseCSVError(CourseCsvDto courseCsvDto) {
		String errorMessage = "";



		if (!courseCsvDto.getNumberOfApplicantsFemale().equalsIgnoreCase("NULL")
				&& !CSVHelper.isNumeric(courseCsvDto.getNumberOfApplicantsFemale())) {
			errorMessage += ", invalid number of applicants female";
		}
		
		if (!courseCsvDto.getNumberOfApplicantsMale().equalsIgnoreCase("NULL")
				&& !CSVHelper.isNumeric(courseCsvDto.getNumberOfApplicantsMale())) {
			errorMessage += ", invalid number of applicants male";
		}
		
		if (!courseCsvDto.getCohortSizeFemale().equalsIgnoreCase("NULL")
				&& !CSVHelper.isNumeric(courseCsvDto.getCohortSizeFemale())) {
			errorMessage += ", invalid cohort size female";
		}
		
		if (!courseCsvDto.getCohortSizeMale().equalsIgnoreCase("NULL")
				&& !CSVHelper.isNumeric(courseCsvDto.getCohortSizeMale())) {
			errorMessage += ", invalid cohort size male";
		}
		
		if (!courseCsvDto.getNumberOfCertifiedFemale().equalsIgnoreCase("NULL")
				&& !CSVHelper.isNumeric(courseCsvDto.getNumberOfCertifiedFemale())) {
			errorMessage += ", invalid number of certified female";
		}
		
		if (!courseCsvDto.getNumberOfCertifiedMale().equalsIgnoreCase("NULL")
				&& !CSVHelper.isNumeric(courseCsvDto.getNumberOfCertifiedMale())) {
			errorMessage += ", invalid number of certified male";
		}

		
		if (!courseCsvDto.getStartDate().equalsIgnoreCase("NULL")
				&& !CSVHelper.validateDateFormat(courseCsvDto.getStartDate(), ConfigData.DateFormat)) {
			errorMessage += ", invalid created at date format";
		}

		if (!courseCsvDto.getEndDate().equalsIgnoreCase("NULL")
				&& !CSVHelper.validateDateFormat(courseCsvDto.getEndDate(), ConfigData.DateFormat)) {
			errorMessage += ", invalid end date format";
		}
		
		if(!courseCsvDto.getTrainerId().equalsIgnoreCase("NULL") && !courseCsvDto.getTrainerId().isEmpty()) {
			if(!trainerRepo.isExistTrainerId(courseCsvDto.getTrainerId())) {
				errorMessage += ", invalid Trainer Id";
			}
		}
		
		if(!courseCsvDto.getCId().equalsIgnoreCase("NULL") && !courseCsvDto.getCId().isEmpty()) {
			if(!studentRepo.isExistCidNumber(courseCsvDto.getCId())) {
				errorMessage += ", invalid CID";
			}
		}
		
		if(!courseCsvDto.getDId().equalsIgnoreCase("NULL") && !courseCsvDto.getDId().isEmpty()) {
			if(!studentRepo.isExistDidNumber(courseCsvDto.getDId())) {
				errorMessage += ", invalid DID";
			}
		}
	
		courseCsvDto.setErrorMessage(errorMessage);
		courseCsvDto.setHaveError(errorMessage == "" ? false : true);
	}

	private List<CourseCsvDto> readCourseCSV(String path) throws IllegalStateException, FileNotFoundException {
		List<CourseCsvDto> courseCSVList = new ArrayList<CourseCsvDto>();
		courseCSVList = new CsvToBeanBuilder<CourseCsvDto>(new FileReader(path)).withType(CourseCsvDto.class)
				.withIgnoreLeadingWhiteSpace(true).build().parse();
		return courseCSVList;
	}

	private void writeCourseCSVFile(String filePath, List<CourseCsvDto> errorList) {
		List<String> headerList = ConfigData.CourseCSVHeader;
		CSVWriter csvWriter;
		try {
			csvWriter = new CSVWriter(new FileWriter(filePath));
			String[] headers = (String[]) headerList.toArray();
			csvWriter.writeNext(headers); // write header
			for (CourseCsvDto course : errorList) {
				csvWriter.writeNext(new String[] { course.getCourseId(), course.getCourseName(), course.getStatus(),
						course.getSector(), course.getCourseLevel(), course.getDuration(), course.getStartDate(),
						course.getEndDate(), course.getCohortSizeMale(), course.getCohortSizeFemale(),
						course.getNumberOfApplicantsMale(), course.getNumberOfApplicantsFemale(), course.getNumberOfCertifiedMale(), course.getNumberOfCertifiedFemale(),
						course.getBatchNo(), course.getTrainingLoaction(), course.getTrainerId(),
						course.getTrainerId(), course.getStudentName(), course.getCId(),course.getDId(),
						course.getErrorMessage() });
			}
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/* Trainer Upload */
	private void checkTrainerCSVError(TrainerCsvDto tainerCsvDto) {
		String errorMessage = "";
		if (tainerCsvDto.getTrainerId() == null || tainerCsvDto.getTrainerId().isEmpty()) {
			errorMessage += "Trainer id is empty";
		}
		
		if (tainerCsvDto.getTrainerName() == null || tainerCsvDto.getTrainerName().isEmpty()) {
			errorMessage += "Trainer name is empty";
		}

		if (!CSVHelper.validateDateFormat(tainerCsvDto.getJoinDate(), ConfigData.DateFormat)) {
			errorMessage += "Trainer name is empty";
		}
	}

	private List<TrainerCsvDto> readTrainerCSV(String path) throws IllegalStateException, FileNotFoundException { // read
		List<TrainerCsvDto> trainerCSVList = new ArrayList<TrainerCsvDto>();
		trainerCSVList = new CsvToBeanBuilder<TrainerCsvDto>(new FileReader(path)).withType(TrainerCsvDto.class)
				.withIgnoreLeadingWhiteSpace(true).build().parse();
		return trainerCSVList;
	}

	private GenericResponse uploadStudentCSV(MultipartFile file) {
		// TODO Auto-generated method stub
		if (CSVHelper.validateCSVFile(file, ConfigData.StudentCSVHeader)) {

			int successCount = 0;
			int failCount = 0;

			Long uploadFileId = saveUploadFile(file.getOriginalFilename(), "student");
			Boolean checkFileSave = CSVHelper.saveFile(file, uploadFileId.toString(), uploadPath);
			if (!checkFileSave) {
				return new GenericResponse(false, ResponseMessage.FILE_SAVE_ERROR);
			}

			String uploadFilePath = uploadPath + File.separator + uploadFileId.toString() + ".csv";

			// read csv file
			List<StudentCsvDto> studentCSVList = null;
			try {
				studentCSVList = readStudentCSV(uploadFilePath);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return new GenericResponse(false, ResponseMessage.INVALID_UPLOAD);
			}
			List<String> cidList = studentCSVList.stream().map(a -> a.getCid()).collect(Collectors.toList());
			List<String> didList = studentCSVList.stream().map(a -> a.getDid()).collect(Collectors.toList());

			if (CSVHelper.isHaveDuplicate(cidList)) {
				return new GenericResponse(false, "Some cid value duplicate in upload file");
			}

			if (CSVHelper.isHaveDuplicate(didList)) {
				return new GenericResponse(false, "Some did value duplicate in upload file");
			}

			Role studentRole = roleRepo.findByName(ERole.ROLE_STUDENT).orElse(null);

			List<Student> saveStudentList = new ArrayList<Student>();
			List<Student> updateStudentList = new ArrayList<Student>();
			List<StudentCsvDto> errorList = new ArrayList<StudentCsvDto>();
		
			for (StudentCsvDto student : studentCSVList) {
				checkStudentCSVError(student); // validate student csv file
				if (student.isHaveError()) {
					errorList.add(student); // add error list
					failCount++;
				} else { // insert student
					Student saveObj;
					try {
						if (!CSVHelper.isNumeric(student.getId())) { // add to save list
							String userName = student.getName() + "-" + student.getCid();
							String password = commonUtil.generatePassword();
							User user = new User(userName, student.getEmail(), encoder.encode(password), new Date(), new Date(), studentRole);
							saveObj = student.getEntity(user);
							saveStudentList.add(saveObj);
						} else { // add to update list
							saveObj = student.getEntity(null);
							updateStudentList.add(saveObj);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					successCount++;
				}
			}
			if (updateStudentList.size() > 0) { // update
				studentRepo.saveAll(updateStudentList);
			}

			if (saveStudentList.size() > 0) { // insert
				studentRepo.saveAll(saveStudentList);
			}

			if (failCount > 0) { // create error csv file
				String errroFilePath = uploadPath + File.separator + uploadFileId.toString() + ConfigData.errorFileName;
				writeStudentCSVFile(errroFilePath, errorList);
			}

			UploadFileRecord existingUploadFileRecord = uploadFileRepo.findById(uploadFileId).orElse(null);
			if (existingUploadFileRecord != null) {
				existingUploadFileRecord.setTotalRecord(successCount + failCount);
				existingUploadFileRecord.setFailRecord(failCount);
				existingUploadFileRecord.setSuccessRecord(successCount);
				existingUploadFileRecord.setErrorFileName(uploadFileId.toString() + ConfigData.errorFileName);
				uploadFileRepo.save(existingUploadFileRecord);
			}
			UploadResultDto uploadResultDto = new UploadResultDto();
			uploadResultDto.setTotalCount(successCount + failCount);
			uploadResultDto.setFailCount(failCount);
			uploadResultDto.setSuccessCount(successCount);
			uploadResultDto.setStudentErrorList(errorList);
			uploadResultDto.setUploadType("Student");
			return new GenericResponse(true, uploadResultDto);

		} else {
			return new GenericResponse(false, ResponseMessage.INVALID_UPLOAD);
		}

	}

	private GenericResponse uploadCourse(MultipartFile file) {
		if (CSVHelper.validateCSVFile(file, ConfigData.CourseCSVHeader)) {
			int successCount = 0;
			int failCount = 0;

			Long uploadFileId = saveUploadFile(file.getOriginalFilename(), "course");
			Boolean checkFileSave = CSVHelper.saveFile(file, uploadFileId.toString(), uploadPath);
			if (!checkFileSave) {
				return new GenericResponse(false, ResponseMessage.FILE_SAVE_ERROR);
			}

			String uploadFilePath = uploadPath + File.separator + uploadFileId.toString() + ".csv";

			// read csv file
			List<CourseCsvDto> courseCSVList = null;
			try {
				courseCSVList = readCourseCSV(uploadFilePath);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return new GenericResponse(false, ResponseMessage.INVALID_UPLOAD);
			}
			List<Course> saveCourseList = new ArrayList<Course>();
			List<CourseCsvDto> errorList = new ArrayList<CourseCsvDto>();

			for (CourseCsvDto courseCsvDto : courseCSVList) {
				checkCourseCSVError(courseCsvDto);
				if (courseCsvDto.isHaveError()) { // have error
					errorList.add(courseCsvDto);
					failCount++;
				} else {
					try {
						saveCourseList.add(courseCsvDto.getEntity());
						successCount++;
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			if (saveCourseList.size() > 0) {
				courseRepo.saveAll(saveCourseList);
			}
			if(errorList.size() > 0) {
				String errroFilePath = uploadPath + File.separator + uploadFileId.toString() + ConfigData.errorFileName;
				writeCourseCSVFile(errroFilePath, errorList);
			}
			UploadResultDto uploadResultDto = new UploadResultDto();
			uploadResultDto.setTotalCount(successCount + failCount);
			uploadResultDto.setFailCount(failCount);
			uploadResultDto.setSuccessCount(successCount);
			uploadResultDto.setCourseErrorList(errorList);
			uploadResultDto.setUploadType("Course");
			return new GenericResponse(true, uploadResultDto);
		} else {
			return new GenericResponse(false, ResponseMessage.INVALID_UPLOAD);
		}
	}

	private GenericResponse uploadTrainer(MultipartFile file) {
		if (CSVHelper.validateCSVFile(file, ConfigData.TrainerCSVHeader)) {
			int successCount = 0;
			int failCount = 0;

			Long uploadFileId = saveUploadFile(file.getOriginalFilename(), "trainer");
			Boolean checkFileSave = CSVHelper.saveFile(file, uploadFileId.toString(), uploadPath);
			if (!checkFileSave) {
				return new GenericResponse(false, ResponseMessage.FILE_SAVE_ERROR);
			}

			String uploadFilePath = uploadPath + File.separator + uploadFileId.toString() + ".csv";

			// read csv file
			List<TrainerCsvDto> trainerCSVList = new ArrayList<TrainerCsvDto>();
			List<TrainerCsvDto> errorList = new ArrayList<TrainerCsvDto>();
			List<Trainer> saveTrainerList = new ArrayList<Trainer>();

			try {
				trainerCSVList = readTrainerCSV(uploadFilePath);
				for (TrainerCsvDto trainerCsvDto : trainerCSVList) {
					checkTrainerCSVError(trainerCsvDto);
					if (trainerCsvDto.isHaveError()) {
						errorList.add(trainerCsvDto);
						failCount++;
					} else {
						try {
							saveTrainerList.add(trainerCsvDto.getEntity());
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						successCount++;
					}

				}

			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new GenericResponse(false, ResponseMessage.SERVER_ERROR);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new GenericResponse(false, ResponseMessage.FILE_NOT_FOUND);
			}
			if (saveTrainerList.size() > 0) {
				trainerRepo.saveAll(saveTrainerList);
			}
			UploadResultDto uploadResultDto = new UploadResultDto();
			uploadResultDto.setTotalCount(successCount + failCount);
			uploadResultDto.setFailCount(failCount);
			uploadResultDto.setSuccessCount(successCount);
			uploadResultDto.setTrainerErrorList(errorList);
			uploadResultDto.setUploadType("Trainer");
			return new GenericResponse(true, uploadResultDto);
		} else {
			return new GenericResponse(false, ResponseMessage.INVALID_UPLOAD);
		}
	}

	@Override
	public GenericResponse uploadData(MultipartFile file, String uploadType) {

		if (uploadType.equalsIgnoreCase("student")) {
			return uploadStudentCSV(file);
		}
		if (uploadType.equalsIgnoreCase("course")) {
			return uploadCourse(file);
		}
		if (uploadType.equalsIgnoreCase("trainer")) {
			return uploadTrainer(file);
		}
		return new GenericResponse(false, ResponseMessage.INVALID_UPLOAD);
	}

	@Override
	public UploadFileRecord getUploadFileRecordById(Long id) {
		// TODO Auto-generated method stub
		return uploadFileRepo.getById(id);
	}

	@Override
	public List<UploadFileRecordDto> getUploadHistory() {
		List<UploadFileRecordDto> uploadFileDtoLisr = new ArrayList<UploadFileRecordDto>();
		List<UploadFileRecord> uploadList = uploadFileRepo.findAll();
		for (UploadFileRecord uploadFileRecord : uploadList) {
			uploadFileDtoLisr.add(new UploadFileRecordDto(uploadFileRecord));
		}
		return uploadFileDtoLisr;
	}

}
