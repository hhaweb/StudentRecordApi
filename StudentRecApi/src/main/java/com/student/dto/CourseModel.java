package com.student.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class CourseModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String courseId;
	private String courseName;
	
	private String status;
	private String courseLevel;
	private Date startDate;
	private Date endDate;
	private Integer cohortSizeMale;
	private Integer cohortSizeFemale;
	private String batchNo;
	private String trainingLoaction;
	private Long totalRecords;
	private String duration;
	public CourseModel(Long id,String courseId, String courseName, String status, String courseLevel, Date startDate,
			Date endDate, Integer cohortSizeMale, Integer cohortSizeFemale, String batchNo, String trainingLoaction) {
		super();
		this.id = id;
		this.courseId = courseId;
		this.courseName = courseName;
		this.status = status;
		this.courseLevel = courseLevel;
		this.startDate = startDate;
		this.endDate = endDate;
		this.cohortSizeMale = cohortSizeMale;
		this.cohortSizeFemale = cohortSizeFemale;
		this.batchNo = batchNo;
		this.trainingLoaction = trainingLoaction;
	}
	public CourseModel(String courseId, String courseName, String courseLevel, Date startDate, Date endDate,
			String duration) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
		this.courseLevel = courseLevel;
		this.startDate = startDate;
		this.endDate = endDate;
		this.duration = duration;
	}

	
	
	

	
}
