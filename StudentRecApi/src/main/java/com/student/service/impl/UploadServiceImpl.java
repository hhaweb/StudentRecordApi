package com.student.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.student.config.ConfigData;
import com.student.config.ERole;
import com.student.config.ResponseMessage;
import com.student.dto.UploadHistoryDto;
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
import com.student.entity.UploadHistory;
import com.student.entity.User;
import com.student.repository.CourseRepository;
import com.student.repository.RoleRepository;
import com.student.repository.StudentRepository;
import com.student.repository.TrainerRepository;
import com.student.repository.UploadHistoryRepository;
import com.student.repository.UserRepository;
import com.student.service.UploadService;
import com.student.util.CSVHelper;
import com.student.util.CommonUtil;
import com.student.util.DateUtil;

@Service
public class UploadServiceImpl implements UploadService {

	@Autowired
	private UploadHistoryRepository uploadFileRepo;

	@Autowired
	private StudentRepository studentRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private CommonUtil commonUtil;

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
		UploadHistory uploadFileRecord = new UploadHistory(fileName, 0, 0, 0, new Date(), loginUser.getUsername(),
				type);
		UploadHistory saveObj = uploadFileRepo.saveAndFlush(uploadFileRecord);
		return saveObj.getId();
	}

	private void checkStudentCSVError(StudentCsvDto student) {
		String errorMessage = "";

		// for empty check
//		if (student.getId() == null || student.getId().isEmpty()) {
//			errorMessage += ", id is empty";
//
//		} else if (!CSVHelper.isNumeric(student.getId())) {
//			errorMessage += ", invalid id";
//		}

		if (student.getCid().equalsIgnoreCase("11608003843")) {
			System.out.print("enter ");
			String aa = student.getMartialStatus().trim();
			System.out.print("enter " + aa);
		}

		if (student.getCid() == null) {
			errorMessage += "cid is empty";
		}

		if (student.getDid() == null) {
			errorMessage += ", did is empty";
		}

		if (student.getGender() != null && student.getGender().equalsIgnoreCase("NULL")
				&& !student.getGender().isEmpty()) {
			String gender = student.getGender().trim();
			Long count = ConfigData.Gender_List.stream().filter(a -> a.equalsIgnoreCase(gender)).count();
			if (count == 0) {
				errorMessage += ", invalid gender";
			} else {
				student.setGender(gender);
			}

		}

		if (student.getBloodGroup() != null && !student.getBloodGroup().equalsIgnoreCase("NULL")
				&& !student.getBloodGroup().isEmpty()) {
			String bloodGroup = student.getBloodGroup().trim();
			Long count = ConfigData.Blood_Group.stream().filter(a -> a.equalsIgnoreCase(bloodGroup)).count();
			if (count == 0) {
				errorMessage += ", invalid blood group";
			} else {
				student.setBloodGroup(bloodGroup);
			}
		}

		if (student.getMartialStatus() != null && !student.getMartialStatus().equalsIgnoreCase("NULL")
				&& !student.getMartialStatus().isEmpty()) {
			String martialStatus = student.getMartialStatus().trim();
			Long count = ConfigData.Martial_Status.stream().filter(a -> a.equalsIgnoreCase(martialStatus)).count();
			if (count == 0) {
				errorMessage += ", invalid martial status";
			} else {
				student.setMartialStatus(martialStatus);
			}
		}
		if (student.getCid().equalsIgnoreCase("11406000335")) {
			String aa = "ssaadssa";
		}

		if (!DateUtil.validateDateFormat(student.getDateOfBirth())) {
			errorMessage += ", invalid date of birth date format";
		} else {
			int age = DateUtil.getDiffYears(DateUtil.stringToDate(student.getDateOfBirth()), new Date());
			if (age < 16) {
				errorMessage += ", Date Of birth must be above 16 years old";
			}
		}

		// valid date format
//		if (!CSVHelper.validateDateFormat(student.getDateOfBirth(), ConfigData.DateFormat)
//				&& !student.getDateOfBirth().equalsIgnoreCase("NULL")) {
//			errorMessage += ", invalid date of birth  format";
//		}

//		if (!CSVHelper.validateDateFormat(student.getCreatedAt(), ConfigData.DateFormatWithTime)
//				&& !student.getCreatedAt().equalsIgnoreCase("NULL")) {
//			errorMessage += ", invalid created at date format";
//		}
//
//		if (!CSVHelper.validateDateFormat(student.getUpdatedAt(), ConfigData.DateFormatWithTime)
//				&& !student.getUpdatedAt().equalsIgnoreCase("NULL")) {
//			errorMessage += ", invalid created at date format";
//		}
//
//		if (!CSVHelper.validateDateFormat(student.getDeletedAt(), ConfigData.DateFormatWithTime)
//				&& !student.getDeletedAt().equalsIgnoreCase("NULL")) {
//			errorMessage += ", invalid deleted at date format";
//		}

		// for already exit check

		if (CSVHelper.isNumeric(student.getId())) {
			if (!studentRepo.existsById(Long.parseLong(student.getId()))) {
				errorMessage += ", id doesn't exist";
			}
			if (studentRepo.isExistCidNumberById(student.getCid(), Long.parseLong(student.getId()))) {
				errorMessage += ", cid is already exist";
			}

			if (studentRepo.isExistDidNumberById(student.getDid(), Long.parseLong(student.getId()))) {
				errorMessage += ", did is already exist";
			}
		} else {
			if (studentRepo.isExistDidNumber(student.getDid())) {
				errorMessage += ", did is already exist";
			}
			if (studentRepo.isExistCidNumber(student.getCid())) {
				errorMessage += ", cid is already exist";
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
		String[] headers = ConfigData.StudentCSVHeaderError;
		CSVWriter csvWriter;
		try {
			csvWriter = new CSVWriter(new FileWriter(filePath));
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
		if (courseCsvDto.getNumberOfApplicantsFemale() != null
				&& !courseCsvDto.getNumberOfApplicantsFemale().trim().isEmpty()
				&& !CSVHelper.isNumeric(courseCsvDto.getNumberOfApplicantsFemale())) {
			errorMessage += ", invalid number of applicants female";
		}

		if (courseCsvDto.getNumberOfApplicantsMale() != null
				&& !courseCsvDto.getNumberOfApplicantsMale().trim().isEmpty()
				&& !CSVHelper.isNumeric(courseCsvDto.getNumberOfApplicantsMale())) {
			errorMessage += ", invalid number of applicants male";
		}

		if (courseCsvDto.getCohortSizeFemale() != null && !courseCsvDto.getCohortSizeFemale().trim().isEmpty()
				&& !CSVHelper.isNumeric(courseCsvDto.getCohortSizeFemale())) {
			errorMessage += ", invalid cohort size female";
		}

		if (courseCsvDto.getCohortSizeMale() != null && !courseCsvDto.getCohortSizeMale().trim().isEmpty()
				&& !CSVHelper.isNumeric(courseCsvDto.getCohortSizeMale())) {
			errorMessage += ", invalid cohort size male";
		}

		if (courseCsvDto.getNumberOfCertifiedFemale() != null
				&& !courseCsvDto.getNumberOfCertifiedFemale().trim().isEmpty()
				&& !CSVHelper.isNumeric(courseCsvDto.getNumberOfCertifiedFemale())) {
			errorMessage += ", invalid number of certified female";
		}

		if (courseCsvDto.getNumberOfCertifiedMale() != null && !courseCsvDto.getNumberOfCertifiedMale().trim().isEmpty()
				&& !CSVHelper.isNumeric(courseCsvDto.getNumberOfCertifiedMale())) {
			errorMessage += ", invalid number of certified male";
		}

		if (courseCsvDto.getStartDate() == null) {
			errorMessage += ", start date is empty";
		} else if (!DateUtil.validateDateFormat(courseCsvDto.getStartDate())) {
			errorMessage += ", invalid start date format";
		}

		if (courseCsvDto.getEndDate() == null) {
			errorMessage += ", end date is empty";
		} else if (!DateUtil.validateDateFormat(courseCsvDto.getEndDate())) {
			errorMessage += ", invalid end date format";
		}

		if (courseCsvDto.getTrainerId() != null && !courseCsvDto.getTrainerId().isEmpty()) {
			if (!trainerRepo.isExistTrainerId(courseCsvDto.getTrainerId())) {
				errorMessage += ", invalid Trainer Id";
			}
		}

		if (courseCsvDto.getCId() != null && !courseCsvDto.getCId().isEmpty()) {
			if (!studentRepo.isExistCidNumber(courseCsvDto.getCId())) {
				errorMessage += ", invalid CID";
			}
		}

		if (courseCsvDto.getDId() != null && !courseCsvDto.getDId().isEmpty()) {
			if (!studentRepo.isExistDidNumber(courseCsvDto.getDId())) {
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
		String[] headers = ConfigData.CourseCSVHeaderError;
		CSVWriter csvWriter;
		try {
			csvWriter = new CSVWriter(new FileWriter(filePath));
			csvWriter.writeNext(headers); // write header
			for (CourseCsvDto course : errorList) {
				csvWriter.writeNext(new String[] { course.getCourseId(), course.getCourseName(), course.getStatus(),
						course.getSector(), course.getCourseLevel(), course.getDuration(), course.getStartDate(),
						course.getEndDate(), course.getCohortSizeMale(), course.getCohortSizeFemale(),
						course.getNumberOfApplicantsMale(), course.getNumberOfApplicantsFemale(),
						course.getNumberOfCertifiedMale(), course.getNumberOfCertifiedFemale(), course.getBatchNo(),
						course.getTrainingLoaction(), course.getTrainerId(), course.getStudentName(), course.getCId(),
						course.getDId(), course.getErrorMessage() });
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
		} else {
			if (trainerRepo.isExistTrainerId(tainerCsvDto.getTrainerId())) {
				errorMessage += "Trainer id is already exist";
			}
		}

		if (tainerCsvDto.getTrainerName() == null || tainerCsvDto.getTrainerName().isEmpty()) {
			errorMessage += "Trainer name is empty";
		}

		if (tainerCsvDto.getGender() != null && !tainerCsvDto.getGender().equalsIgnoreCase("NULL")
				&& !tainerCsvDto.getGender().isEmpty()) {
			String gender = tainerCsvDto.getGender().trim();
			Long count = ConfigData.Gender_List.stream().filter(a -> a.equalsIgnoreCase(gender)).count();
			if (count == 0) {
				errorMessage += ", invalid gender";
			} else {
				tainerCsvDto.setGender(gender);
			}
		}
//		if (!CSVHelper.validateDateFormat(tainerCsvDto.getJoinDate(), ConfigData.DateFormat)) {
//			errorMessage += "Invalid join date";
//		}
		tainerCsvDto.setErrorMessage(errorMessage);
		tainerCsvDto.setHaveError(errorMessage == "" ? false : true);
	}

	private List<TrainerCsvDto> readTrainerCSV(String path) throws IllegalStateException, FileNotFoundException { // read
		List<TrainerCsvDto> trainerCSVList = new ArrayList<TrainerCsvDto>();
		trainerCSVList = new CsvToBeanBuilder<TrainerCsvDto>(new FileReader(path)).withType(TrainerCsvDto.class)
				.withIgnoreLeadingWhiteSpace(true).build().parse();
		return trainerCSVList;
	}

	private void writeTrainerCSVErrorFile(String filePath, List<TrainerCsvDto> errorList) {
		String[] headerList = ConfigData.TrainerCSVHeaderError;
		CSVWriter csvWriter;
		try {
			csvWriter = new CSVWriter(new FileWriter(filePath));
			csvWriter.writeNext(headerList); // write header
			for (TrainerCsvDto trainer : errorList) {
				csvWriter.writeNext(new String[] { trainer.getTrainerId(), trainer.getTrainerName(),
						trainer.getGender(), trainer.getNationality(), trainer.getJoinDate(), trainer.getDesignation(),
						trainer.getDepartment(), trainer.getBranch(), trainer.getDspCenter(),
						trainer.getTrainingProgramme(), trainer.getAffiliation(), trainer.getQualification(),
						trainer.getErrorMessage() });
			}
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			List<StudentCsvDto> errorList = new ArrayList<StudentCsvDto>();
			List<User> saveUserList = new ArrayList<User>();
			for (StudentCsvDto student : studentCSVList) {
				checkStudentCSVError(student); // validate student csv file
				if (student.isHaveError()) {
					errorList.add(student); // add error list
					failCount++;
				} else { // insert student
					Student saveObj;
					try {
						// String userName = student.getName() + "-" + student.getCid();
						String userName = student.getCid();
						saveObj = student.getEntity();
						saveStudentList.add(saveObj);
						if (!userRepo.existsByUserName(userName)) {
							User user = new User(userName, student.getEmail(), null, new Date(), new Date(),
									studentRole);
							saveUserList.add(user);
						}

//						if (!CSVHelper.isNumeric(student.getId()) ) { // add to save list
//							String userName = student.getName() + "-" + student.getCid();
//							String password = commonUtil.generatePassword();
//							User user = new User(userName, student.getEmail(), encoder.encode(password), new Date(),
//									new Date(), studentRole);
//							saveObj = student.getEntity(user);
//							saveStudentList.add(saveObj);
//						} else { // add to update list
//							saveObj = student.getEntity(null);
//							updateStudentList.add(saveObj);
//						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					successCount++;
				}
			}
//			if (updateStudentList.size() > 0) { // update
//				studentRepo.saveAll(updateStudentList);
//			}

			if (saveStudentList.size() > 0) { // insert
				studentRepo.saveAll(saveStudentList);
			}

			if (saveUserList.size() > 0) {
				userRepo.saveAll(saveUserList);
			}
			if (failCount > 0) { // create error csv file
				String errroFilePath = uploadPath + File.separator + uploadFileId.toString() + ConfigData.errorFileName;
				writeStudentCSVFile(errroFilePath, errorList);
			}

			UploadHistory existingUploadFileRecord = uploadFileRepo.findById(uploadFileId).orElse(null);
			if (existingUploadFileRecord != null) {
				existingUploadFileRecord.setTotalRecord(successCount + failCount);
				existingUploadFileRecord.setFailRecord(failCount);
				existingUploadFileRecord.setSuccessRecord(successCount);
				uploadFileRepo.save(existingUploadFileRecord);
			}
			UploadResultDto uploadResultDto = new UploadResultDto();
			uploadResultDto.setTotalCount(successCount + failCount);
			uploadResultDto.setFailCount(failCount);
			uploadResultDto.setSuccessCount(successCount);
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
			if (errorList.size() > 0) {
				String errroFilePath = uploadPath + File.separator + uploadFileId.toString() + ConfigData.errorFileName;
				writeCourseCSVFile(errroFilePath, errorList);
			}

			UploadHistory existingUploadFileRecord = uploadFileRepo.findById(uploadFileId).orElse(null);
			if (existingUploadFileRecord != null) {
				existingUploadFileRecord.setTotalRecord(successCount + failCount);
				existingUploadFileRecord.setFailRecord(failCount);
				existingUploadFileRecord.setSuccessRecord(successCount);
				uploadFileRepo.save(existingUploadFileRecord);
			}

			UploadResultDto uploadResultDto = new UploadResultDto();
			uploadResultDto.setTotalCount(successCount + failCount);
			uploadResultDto.setFailCount(failCount);
			uploadResultDto.setSuccessCount(successCount);
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
			if (errorList.size() > 0) {
				String errroFilePath = uploadPath + File.separator + uploadFileId.toString() + ConfigData.errorFileName;
				writeTrainerCSVErrorFile(errroFilePath, errorList);
			}

			UploadHistory existingUploadFileRecord = uploadFileRepo.findById(uploadFileId).orElse(null);
			if (existingUploadFileRecord != null) {
				existingUploadFileRecord.setTotalRecord(successCount + failCount);
				existingUploadFileRecord.setFailRecord(failCount);
				existingUploadFileRecord.setSuccessRecord(successCount);
				uploadFileRepo.save(existingUploadFileRecord);
			}
			UploadResultDto uploadResultDto = new UploadResultDto();
			uploadResultDto.setTotalCount(successCount + failCount);
			uploadResultDto.setFailCount(failCount);
			uploadResultDto.setSuccessCount(successCount);
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
	public UploadHistoryDto getUploadHistoryById(Long id) {
		UploadHistoryDto uploadHistoryDto = null;
		UploadHistory history = uploadFileRepo.findById(id).orElse(null);
		if (history != null) {
			uploadHistoryDto = new UploadHistoryDto(history);
		}
		return uploadHistoryDto;
	}

	@Override
	public List<UploadHistoryDto> getUploadHistory() {
		List<UploadHistoryDto> uploadFileDtoLisr = new ArrayList<UploadHistoryDto>();
		List<UploadHistory> uploadList = uploadFileRepo.getAllUploadHistory();
		for (UploadHistory uploadFileRecord : uploadList) {
			uploadFileDtoLisr.add(new UploadHistoryDto(uploadFileRecord));
		}
		return uploadFileDtoLisr;
	}

}
