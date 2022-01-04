package com.student.entity;

import java.io.Serializable;
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
@Table(name = "employment")
@Getter
@Setter
@NoArgsConstructor
public class Employment implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "cid")
	private String cid;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "organization_type")
	private String organizationType;
	
	@Column(name = "current_salary")
	private Integer currentSalary;
	
	@Column(name = "organization_name")
	private String organizationName;
	
	@Column(name = "place_of_work")
	private String placeOfWork;
	
	@Column(name = "expect_work_year")
	private String expectedWorkingYear;
	
	@Column(name = "expect_continue_work")
	private String expectedCoutinueToWork;
	
	@Column(name = "job_specification")
	private String jobSpecification;
	
	@Column(name = "to_join_another_trainig")
	private String toJoinAnotherTraining;
	
	@Column(name = "dsp_related_current_job")
	private String dspRelatedToCurrentJob;

}
