package com.student.dto.csv;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.opencsv.bean.CsvBindByName;
import com.student.dto.CourseModel;
import com.student.entity.Course;
import com.student.util.CSVHelper;
import com.student.util.DateUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourseCsvDto {
	@CsvBindByName(column = "Course ID")
	private String courseId;

	@CsvBindByName(column = "Course Name")
	private String courseName;

	@CsvBindByName(column = "Course Status")
	private String status;

	@CsvBindByName(column = "Industrial Sector")
	private String sector;

	@CsvBindByName(column = "Course Level")
	private String courseLevel;

	@CsvBindByName(column = "Duration")
	private String duration;

	@CsvBindByName(column = "Start Date")
	private String startDate;

	@CsvBindByName(column = "End Date")
	private String endDate;

	@CsvBindByName(column = "Cohort Size Male")
	private String cohortSizeMale;

	@CsvBindByName(column = "Cohort Size Female")
	private String cohortSizeFemale;

	@CsvBindByName(column = "NO. Applicants Male")
	private String numberOfApplicantsMale;

	@CsvBindByName(column = "NO. Applicants Female")
	private String numberOfApplicantsFemale;

	@CsvBindByName(column = "NO. Students Certified Male")
	private String numberOfCertifiedMale;

	@CsvBindByName(column = "NO. Students Certified Female")
	private String numberOfCertifiedFemale;

	@CsvBindByName(column = "Batch Number")
	private String batchNo;

	@CsvBindByName(column = "Training Location")
	private String trainingLoaction;

	@CsvBindByName(column = "Trainer ID")
	private String trainerId;

	@CsvBindByName(column = "Student Name") 
	private String studentName;

	@CsvBindByName(column = "CID")
	private String cId;

	@CsvBindByName(column = "DID")
	private String dId;

	@CsvBindByName(column = "Remark")
	private String errorMessage;

	private boolean isHaveError;

//	@CsvBindByName(column = "trainer name")
//	private String trainerName;
//	
//	@CsvBindByName(column = "trainer nationality")
//	private String trainerNationality;
//	
//	@CsvBindByName(column = "trainer affiliation")
//	private String trainerAffiliation;

	public Course getEntity() throws ParseException {
		Course course = new Course();

		if (CSVHelper.isNumeric(this.numberOfApplicantsFemale)) {
			course.setNumberOfApplicantsFemale(Integer.parseInt(this.numberOfApplicantsFemale));
		}
		if (CSVHelper.isNumeric(this.numberOfApplicantsMale)) {
			course.setNumberOfApplicantsMale(Integer.parseInt(this.numberOfApplicantsMale));
		}

		if (CSVHelper.isNumeric(this.cohortSizeFemale)) {
			course.setCohortSizeFemale(Integer.parseInt(this.cohortSizeFemale));
		}
		if (CSVHelper.isNumeric(this.cohortSizeMale)) {
			course.setCohortSizeMale(Integer.parseInt(this.cohortSizeMale));
		}

		if (CSVHelper.isNumeric(this.numberOfCertifiedFemale)) {
			course.setNumberOfCertifiedFemale(Integer.parseInt(this.numberOfCertifiedFemale));
		}

		if (CSVHelper.isNumeric(this.numberOfCertifiedMale)) {
			course.setNumberOfCertifiedFemale(Integer.parseInt(this.numberOfCertifiedMale));
		}

		if (!this.batchNo.equalsIgnoreCase("NULL") && !this.batchNo.isEmpty()) {
			course.setBatchNo(this.batchNo);
		}

		if (!this.trainerId.equalsIgnoreCase("NULL") && !this.trainerId.isEmpty()) {
			course.setTrainerId(this.trainerId);
		}
		
		if (!this.courseId.equalsIgnoreCase("NULL") && !this.courseId.isEmpty()) {
			course.setCourseId(this.courseId);
		}

		if (!this.courseName.equalsIgnoreCase("NULL") && !this.courseName.isEmpty()) {
			course.setCourseName(this.courseName);
		}
		if (!this.status.equalsIgnoreCase("NULL") && !this.status.isEmpty()) {
			course.setStatus(this.status);
		}
		if (!this.sector.equalsIgnoreCase("NULL") && !this.sector.isEmpty()) {
			course.setSector(this.sector);
		}

		if (!this.courseLevel.equalsIgnoreCase("NULL") && !this.courseLevel.isEmpty()) {
			course.setCourseLevel(this.courseLevel);
		}

		if (!this.duration.equalsIgnoreCase("NULL") && !this.duration.isEmpty()) {
			course.setDuration(this.duration);
		}

		if (!this.trainingLoaction.equalsIgnoreCase("NULL") && !this.trainingLoaction.isEmpty()) {
			course.setTrainingLoaction(this.trainingLoaction);
		}

		if (!this.trainerId.equalsIgnoreCase("NULL") && !this.trainerId.isEmpty()) {
			course.setTrainerId(this.trainerId);
		}
		if (!this.cId.equalsIgnoreCase("NULL") && !this.cId.isEmpty()) {
			course.setCId(this.cId);
		}
		if (!this.dId.equalsIgnoreCase("NULL") && !this.dId.isEmpty()) {
			course.setDId(this.dId);
		}
		course.setStartDate(DateUtil.stringToDate(this.startDate));
		course.setEndDate(DateUtil.stringToDate(this.endDate));
		return course;
	}

	public CourseCsvDto(CourseModel courseDto) {
		DateFormat df = new SimpleDateFormat();
		this.courseId = courseDto.getCourseId();
		this.courseName = courseDto.getCourseName();
		this.status = courseDto.getStatus();
	//	this.sector = courseDto.getSector();
		this.courseLevel = courseDto.getCourseLevel();
		this.duration = courseDto.getDuration();
		if(courseDto.getStartDate() != null) {
			this.startDate = df.format(courseDto.getStartDate());
		}
		if(courseDto.getEndDate() != null) {
			this.endDate = df.format(courseDto.getEndDate());
		}
		this.cohortSizeMale = courseDto.getCohortSizeMale() != null ? courseDto.getCohortSizeMale().toString() : null;
		this.cohortSizeFemale = courseDto.getCohortSizeFemale() != null ? courseDto.getCohortSizeFemale().toString()
				: null;
//		this.numberOfApplicantsMale = courseDto.getNumberOfApplicantsMale() != null
//				? courseDto.getNumberOfApplicantsMale().toString()
//				: null;
//		this.numberOfApplicantsFemale = courseDto.getNumberOfApplicantsFemale() != null
//				? courseDto.getNumberOfApplicantsFemale().toString()
//				: null;
//		this.numberOfCertifiedMale = courseDto.getNumberOfCertifiedMale() != null
//				? courseDto.getNumberOfCertifiedMale().toString()
//				: null;
//		this.numberOfCertifiedFemale = courseDto.getNumberOfCertifiedFemale() != null
//				? courseDto.getNumberOfCertifiedFemale().toString()
//				: null;
		this.batchNo = courseDto.getBatchNo();
		this.trainingLoaction = courseDto.getTrainingLoaction();
//		this.trainerId = courseDto.getTrainerId();
//		this.cId = courseDto.getCId();
//		this.dId = courseDto.getDId();
	}

}
