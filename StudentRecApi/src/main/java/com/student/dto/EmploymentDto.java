package com.student.dto;

import java.io.Serializable;

import com.student.entity.Employment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class EmploymentDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String cid;
	private String status;
	private String organizationType;
	private Integer currentSalary;
	private String organizationName;
	private String placeOfWork;
	private String expectedWorkingYear;
	private String expectedCoutinueToWork;
	private String jobSpecification;
	private String toJoinAnotherTraining;
	private String dspRelatedToCurrentJob;
	
	public Employment getEntity() {
		Employment employment =new Employment();
		if(this.id != null && this.id >0) {
			employment.setId(this.id);
		}
		employment.setCid(this.cid);
		employment.setStatus(this.status);
		employment.setOrganizationType(this.organizationType);
		employment.setOrganizationName(this.organizationName);
		employment.setCurrentSalary(this.currentSalary);
		employment.setPlaceOfWork(this.placeOfWork);
		employment.setExpectedWorkingYear(this.expectedWorkingYear);
		employment.setExpectedCoutinueToWork(this.expectedCoutinueToWork);
		employment.setJobSpecification(this.jobSpecification);
		employment.setToJoinAnotherTraining(this.toJoinAnotherTraining);
		employment.setDspRelatedToCurrentJob(this.dspRelatedToCurrentJob);
		return employment;	
	}

	public EmploymentDto(Employment employment) {
		super();
		this.id = employment.getId();
		this.cid = employment.getCid();
		this.status = employment.getStatus();
		this.organizationType = employment.getOrganizationType();
		this.currentSalary = employment.getCurrentSalary();
		this.organizationName = employment.getOrganizationName();
		this.placeOfWork = employment.getPlaceOfWork();
		this.expectedWorkingYear = employment.getExpectedWorkingYear();
		this.expectedCoutinueToWork = employment.getExpectedCoutinueToWork();
		this.jobSpecification = employment.getJobSpecification();
		this.toJoinAnotherTraining = employment.getToJoinAnotherTraining();
		this.dspRelatedToCurrentJob = employment.getDspRelatedToCurrentJob();
	}
	
	
}
