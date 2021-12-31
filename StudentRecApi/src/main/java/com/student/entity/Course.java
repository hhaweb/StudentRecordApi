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
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
public class Course implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name="course_id")
	private String courseId;
	
	@Column(name="course_name")
	private String courseName;
	
	@Column(name="course_status")
	private String status;
	
	@Column(name = "sector")
	private String sector;
	
	@Column(name = "course_level")
	private String courseLevel;
	
	@Column(name = "duration")
	private String duration;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "end_date")
	private Date endDate;
	
	
	@Column(name = "cohort_size_male")
	private Integer cohortSizeMale;
	
	@Column(name = "cohort_size_female")
	private Integer cohortSizeFemale;
	
	@Column(name = "number_of_applicants_male")
	private Integer numberOfApplicantsMale;
	
	@Column(name = "number_of_applicants_female")
	private Integer numberOfApplicantsFemale;
	
	@Column(name = "number_of_certified_male")
	private Integer numberOfCertifiedMale;
	
	@Column(name = "number_of_certified_female")
	private Integer numberOfCertifiedFemale;
	
	@Column(name = "batch_no")
	private String batchNo;
	
	@Column(name = "training_location")
	private String trainingLoaction;
	
	@Column(name = "trainer_id")
	private String trainerId;
	
	@Column(name = "cid")
	private String cId;
	
	@Column(name = "did")
	private String dId;

	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
}
