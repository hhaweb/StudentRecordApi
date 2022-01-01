package com.student.entity;

import java.io.Serializable;
import java.time.Year;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
public class Student implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	private Date dateOfBirth;
	
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
	 
	@Column(name = "avatar")
	private String avatar;
	
	@Column(name = "status")
	private String status;
	
	
	@Column(name = "created_at")
	private Date createdDate;
	
	@Column(name = "updated_at")
	private Date updatedDate;
	
	@Column(name = "deleted_at")
	private Date deletedDate;
	
	@Column(name = "batch_no")
	private Integer batchNo;
	
	@Column(name = "training_year")
	private int trainingYear;
	
	@Column(name = "employment_type_id")
	private Long employmentTypeId;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="system_user_id")
	private User user;

	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "training_center_id")
	private Long trainingCenterId;
	
	@Column(name = "in_date")
	private Date inDate;
	
}
