package com.student.entity;

import java.time.Year;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
public class Course {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
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
	
	
	@Column(name = "cohort_size")
	private String cohortSize;
	
	
	@Column(name = "number_of_applicants")
	private Integer numberOfApplicants;
	
	@Column(name = "batch_no")
	private Integer batchNo;
	
	@Column(name = "training_location")
	private String trainingLoaction;
	
	@Column(name = "trainer_id")
	private Long trainerId;
	
	@Column(name = "trainer_name")
	private String trainerName;
	
	@Column(name = "trainer_nationality")
	private String trainerNationality;
	
	@Column(name = "trainer_affiliation")
	private String trainerAffiliation;
	
	@Column(name = "student_id")
	private Long studentId;
	
}
