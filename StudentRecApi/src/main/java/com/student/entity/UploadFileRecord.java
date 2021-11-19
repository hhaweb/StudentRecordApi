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
@Table(name = "upload_file_record")
@Getter
@Setter
@NoArgsConstructor
public class UploadFileRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "error_file_name")
	private String errorFileName;
	
	@Column(name = "total_record")
	private int totalRecord;
	
	@Column(name = "success_record")
	private int successRecord;
	
	@Column(name = "fail_record")
	private int failRecord;
	
	
	@Column(name = "upload_date")
	private Date uploadDate;
	
	@Column(name = "uploadBy")
	private String uploadBy;

	public UploadFileRecord(String fileName, String errorFileName, int totalRecord, int successRecord, int failRecord,
			Date uploadDate, String uploadBy) {
		super();
		this.fileName = fileName;
		this.errorFileName = errorFileName;
		this.totalRecord = totalRecord;
		this.successRecord = successRecord;
		this.failRecord = failRecord;
		this.uploadDate = uploadDate;
		this.uploadBy = uploadBy;
	}


	
	

}
