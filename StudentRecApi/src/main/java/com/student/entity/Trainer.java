package com.student.entity;

import java.io.Serializable;
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
@Table(name = "trainer")
@Getter
@Setter
@NoArgsConstructor
public class Trainer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
    private Long id;
	
	@Column(name = "trainer_id")
	private String trainerId;
	
	@Column(name = "name")
	private String trainerName;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "nationality")
	private String nationality;
	
	@Column(name = "join_date")
	private Date joinDate;
	
	@Column(name = "designation")
	private String designation;
	
	@Column(name = "department")
	private String department;
	
	@Column(name = "branch")
	private String branch;
	
	@Column(name = "dsp_center")
	private String dspCenter;
	
	@Column(name = "training_programme")
	private String trainingProgramme;
	
	
	@Column(name="affiliation")
	private String affiliation;
	
	@Column(name="qualification")
	private String qualification;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	
}
