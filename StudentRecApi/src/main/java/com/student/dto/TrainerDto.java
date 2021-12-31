package com.student.dto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.student.config.ConfigData;
import com.student.entity.Trainer;
import com.student.util.CSVHelper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainerDto {

    private Long id;
	private String trainerId;
	private String trainerName;
	private String gender;
	private String nationality;
	private String joinDate;
	private String designation;
	private String department;
	private String branch;
	private String dspCenter;
	private String trainingProgramme;
	private String affiliation;
	private String qualification;
	private String createdDate;
	private String updatedDate;
	private Long totalRecords;
	
	
	@JsonIgnore
	public Trainer getEntity() throws ParseException {
		DateFormat df = new SimpleDateFormat(ConfigData.DateFormat);
		Trainer trainer = new Trainer();
		if(this.id != null && this.id != 0) {
			trainer.setId(this.id);
			trainer.setUpdatedDate(new Date());
		} else {
			trainer.setCreatedDate(new Date());
		}
		if(CSVHelper.validateDateFormat(this.createdDate, ConfigData.DateFormat)) {
			trainer.setCreatedDate(df.parse(this.createdDate));
		}
		
		if(CSVHelper.validateDateFormat(this.updatedDate, ConfigData.DateFormat)) {
			trainer.setUpdatedDate(df.parse(this.updatedDate));
		}
		
		if(CSVHelper.validateDateFormat(this.joinDate, ConfigData.DateFormat)) {
			trainer.setJoinDate(df.parse(this.joinDate));
		}
		
		trainer.setTrainerId(this.trainerId);
		trainer.setTrainerName(this.trainerName);
		trainer.setGender(this.gender);
		trainer.setNationality(this.nationality);
		trainer.setDesignation(this.designation);
		trainer.setDepartment(this.department);
		trainer.setBranch(this.branch);
		trainer.setDspCenter(this.dspCenter);
		trainer.setTrainingProgramme(this.trainingProgramme);
		trainer.setAffiliation(this.affiliation);
		trainer.setQualification(this.qualification);	
		return trainer;	
	}

	public TrainerDto(Trainer trainer) {
		DateFormat df = new SimpleDateFormat(ConfigData.DateFormat);
		if(trainer.getCreatedDate() != null) {
			this.createdDate = df.format(trainer.getCreatedDate());
		}
		if(trainer.getUpdatedDate() != null) {
			this.updatedDate = df.format(trainer.getUpdatedDate());
		}
		if(trainer.getJoinDate() != null) {
			this.joinDate = df.format(trainer.getJoinDate());
		}
		
		this.id = trainer.getId();
		this.trainerId = trainer.getTrainerId();
		this.trainerName = trainer.getTrainerName();
		this.gender = trainer.getGender();
		this.nationality = trainer.getNationality();
		this.designation = trainer.getDesignation();
		this.department = trainer.getDepartment();
		this.branch = trainer.getBranch();
		this.dspCenter = trainer.getDspCenter();
		this.trainingProgramme = trainer.getTrainingProgramme();
		this.qualification = trainer.getQualification();
		this.nationality = trainer.getNationality();
		this.affiliation = trainer.getAffiliation();
	
	}
	
	
	
}
