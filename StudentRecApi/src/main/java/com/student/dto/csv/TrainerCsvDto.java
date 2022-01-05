package com.student.dto.csv;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.opencsv.bean.CsvBindByName;
import com.student.config.ConfigData;
import com.student.dto.TrainerDto;
import com.student.entity.Trainer;
import com.student.util.CSVHelper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainerCsvDto {
	@CsvBindByName(column = "Trainer ID")
    private String trainerId;
	
	@CsvBindByName(column = "Trainer Name")
	private String trainerName;
	
	@CsvBindByName(column = "Sex")
	private String gender;
	
	@CsvBindByName(column = "Country")
	private String nationality;
	
	@CsvBindByName(column = "Date of Joining")
	private String joinDate;
	
	@CsvBindByName(column = "Designation")
	private String designation;
	
	@CsvBindByName(column = "Department")
	private String department;
	
	@CsvBindByName(column = "Branch/ Area of Expertise")
	private String branch;
	
	@CsvBindByName(column = "DSP Centre")
	private String dspCenter;
	
	@CsvBindByName(column = "Training Programme")
	private String trainingProgramme;
	
	@CsvBindByName(column = "Trainer Affiliation")
	private String affiliation;
	
	@CsvBindByName(column = "Qualification")
	private String qualification;
	
	
	@CsvBindByName(column = "Remark")
	private String errorMessage;

	private boolean isHaveError;
	
	public Trainer getEntity() throws ParseException {
		Trainer trainer = new Trainer();
		DateFormat df = new SimpleDateFormat(ConfigData.DateFormat);
		trainer.setCreatedDate(new Date());
		trainer.setUpdatedDate(new Date());

		if(!this.trainerId.equalsIgnoreCase("NULL") && !this.trainerId.isEmpty()) {
			trainer.setTrainerId(this.trainerId);
		}

		if(!this.trainerName.equalsIgnoreCase("NULL") && !this.trainerName.isEmpty()) {
			trainer.setTrainerName(this.trainerName);		
		}
		
		if(!this.gender.equalsIgnoreCase("NULL")) {
			trainer.setGender(this.gender);
		}
		
		if(!this.joinDate.equalsIgnoreCase("NULL") && !this.joinDate.isEmpty()) {
			trainer.setJoinDate(df.parse(this.joinDate));
		}
		
		if(!this.designation.equalsIgnoreCase("NULL") && !this.designation.isEmpty()) {
			trainer.setDesignation(this.designation);
		}
		
		if(!this.department.equalsIgnoreCase("NULL")) {
			trainer.setDepartment(this.department);
		}
		
		if(!this.branch.equalsIgnoreCase("NULL")) {
			trainer.setDepartment(this.branch);
		}
		
		if(!this.dspCenter.equalsIgnoreCase("NULL")) {
			trainer.setDspCenter(this.dspCenter);
		}
		
		if(!this.trainingProgramme.equalsIgnoreCase("NULL")) {
			trainer.setTrainingProgramme(this.trainingProgramme);
		}
		
		if(!this.qualification.equalsIgnoreCase("NULL")) {
			trainer.setQualification(this.qualification);
		}
		
		if(!this.nationality.equalsIgnoreCase("NULL")) {
			trainer.setNationality(this.nationality);
		}
		
		if(!this.affiliation.equalsIgnoreCase("NULL")) {
			trainer.setAffiliation(this.affiliation);
		}

		return trainer;
	}

	public TrainerCsvDto(TrainerDto trainer) {
		super();
		this.trainerId = trainer.getTrainerId();
		this.trainerName = trainer.getTrainerName();
		this.gender = trainer.getGender();
		this.nationality = trainer.getNationality();
		this.joinDate = trainer.getJoinDate();
		this.designation = trainer.getDesignation();
		this.department = trainer.getDepartment();
		this.branch = trainer.getBranch();
		this.dspCenter = trainer.getDspCenter();
		this.trainingProgramme = trainer.getTrainingProgramme();
		this.affiliation = trainer.getAffiliation();
		this.qualification = trainer.getQualification();
	}
	
	
	
}
