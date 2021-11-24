package com.student.dto;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.Date;


import com.student.entity.Student;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NewStudentDto {
	
	
	private String avatar;
	
	private int batchNo;
	
	private String bloodGroup;
	
	private String cid;
	
	private Date dateOfBirth;
	
	private String did;
	
	private String email;
	
	private Long employmentTypeId;
	
	private String gender;
	
	private String martialStatus;
	
	private String mobileNo;
	
	private String name;
	
	private String status;
	
	private Long traningCenterId;
	
	private Year trainingYear;
	
	private Long userId;
	
	
	public Student getEntity() {
		Student student = new Student();
		student.setCid(this.cid);
		student.setDid(this.did);
		student.setName(this.name);
		student.setMobileNo(this.mobileNo);
		student.setGender(this.gender);		
		student.setCreatedDate(new Date());
		student.setUserId(this.userId);
		student.setDateOfBith(dateOfBirth);
		return student;
		
	}

}
