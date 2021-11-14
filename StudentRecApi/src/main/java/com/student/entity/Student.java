package com.student.entity;

import java.time.Year;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
public class Student {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
    private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "cid")
	private String cid;
	
	@Column(name = "did")
	private String did;
	
	@Column(name = "date_of_birth")
	private Date dateOfBith;
	
	@NotBlank
	@Email
	@Column(name = "email")
	private String email;
	
	@Column(name = "mobile_no")
	private String mobileNo;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "blood_group")
	private String bloodGroup;
	
	@Column(name = "marital_status")
	private String maritalStatus;
	 
	@Column(name = "employment_type_id")
	private Long employmentTypeId;

	@Column(name = "avatar")
	private String avatar;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "user_id")
	private Long userId;
	
	
	@Column(name = "training_center_id")
	private Long trainingCenterId;
	
	@Column(name = "created_at")
	private Date createdDate;
	
	@Column(name = "updated_at")
	private Date updatedDate;
	
	@Column(name = "deleted_at")
	private Date deletedDate;
	
	@Column(name = "batch_no")
	private Integer batchNo;
	
	@Column(name = "training_year")
	private Year trainingYear;
}
