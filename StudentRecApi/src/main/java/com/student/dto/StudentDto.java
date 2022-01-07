package com.student.dto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.student.config.ConfigData;
import com.student.dto.csv.CourseCsvDto;
import com.student.entity.Course;
import com.student.entity.Student;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentDto {

	private Long id;
	private String name;
	private String cid;
	private String did;
	private String dateOfBirth;
	private String email;
	private String mobileNo;
	private String gender;
	private String bloodGroup;
	private String maritalStatus;
	private String avatar;
	private String status;
	private Long employmentTypeId;
	private Long trainingCenterId;

	private String createdDate;
	private String updatedDate;
	private String deletedDate;
	private String inDate;
	private Long userId; 

	private Integer batchNo;
	private Integer trainingYear;
	private Long totalRecord;
	private List<CourseModel> recommandCourses;
	private List<CourseDto> courseList;
	
	// employment type data
	private EmploymentDto employment;
	
	

	public StudentDto(Student student) {
		DateFormat df = new SimpleDateFormat(ConfigData.DateFormat);
		if (student != null) {
			this.id = student.getId();
			this.name = student.getName();
			this.cid = student.getCid();
			this.did = student.getDid();

			this.email = student.getEmail();
			this.mobileNo = student.getMobileNo();
			this.gender = student.getGender();
			this.bloodGroup = student.getBloodGroup();
			this.maritalStatus = student.getMaritalStatus();
			this.avatar = student.getAvatar();
			this.status = student.getStatus();
			this.batchNo = student.getBatchNo();
			this.employmentTypeId = student.getEmploymentTypeId();
			this.trainingCenterId = student.getTrainingCenterId();
			this.userId = student.getUserId();

			if (student.getDateOfBirth() != null) {
				this.dateOfBirth = df.format(student.getDateOfBirth());
			}

			if (student.getCreatedDate() != null) {
				this.createdDate = df.format(student.getCreatedDate());
			}

			if (student.getUpdatedDate() != null) {
				this.updatedDate = df.format(student.getUpdatedDate());
			}

			if (student.getDeletedDate() != null) {
				this.deletedDate = df.format(student.getDeletedDate());
			}

			if (student.getTrainingYear() != 0) {
				this.trainingYear = student.getTrainingYear();
			}
		}
	}

	@JsonIgnore
	public Student getEntity() throws ParseException {
		DateFormat df = new SimpleDateFormat(ConfigData.DateFormat);
		DateFormat dfWithTime = new SimpleDateFormat(ConfigData.DateFormatWithTime);

		Student student = new Student();

		if (this.id != null && this.id != 0) {
			student.setId(this.id);
		}

		student.setName(this.name);
		student.setCid(this.cid);
		student.setDid(this.did);
		student.setEmail(this.email);
		student.setMobileNo(this.mobileNo);
		student.setGender(this.gender);
		student.setBloodGroup(this.bloodGroup);
		student.setMaritalStatus(this.maritalStatus);
		student.setAvatar(this.avatar);
		student.setStatus(this.status);
		student.setBatchNo(this.batchNo);
		student.setUserId(this.userId);
		
		if(this.trainingCenterId != null) {
			student.setTrainingCenterId(this.trainingCenterId);
		}
		if(this.employmentTypeId != null) {
			student.setEmploymentTypeId(this.employmentTypeId);
		}
		if (this.createdDate != null) {
			student.setCreatedDate(dfWithTime.parse(this.createdDate));
		}
		if (this.updatedDate != null) {
			student.setUpdatedDate(dfWithTime.parse(this.updatedDate));
		}
		if (this.deletedDate != null) {
			student.setDeletedDate(dfWithTime.parse(this.deletedDate));
		}
		if (this.dateOfBirth != null) {
			student.setDateOfBirth(df.parse(this.dateOfBirth));
		}
		if (this.trainingYear != null && this.trainingYear != 0) {
			student.setTrainingYear(this.trainingYear);
		}

		return student;

	}

}
