package com.student.dto;
import java.util.Date;

import com.opencsv.bean.CsvBindByName;
import com.student.entity.Student;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class StudentDto {
	@CsvBindByName(column = "name")
	private String name;
	
	@CsvBindByName(column = "cid")
	private String cid;
	
	@CsvBindByName(column = "did")
	private String did;
	
	@CsvBindByName(column = "mobile_no")
	private String mobileNo;
	
	@CsvBindByName(column = "gender")
	private String gender;
	
	
	@CsvBindByName(column = "perm_address")
	private String permAddress;
	
	@CsvBindByName(column = "Alternative No.")
	private String alternativeNo;
	
	
	@CsvBindByName(column = "Training")
	private String training;
	
	private String errorMessage;
	
	private boolean isHaveError;
	
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
		return student;
		
	}
}
