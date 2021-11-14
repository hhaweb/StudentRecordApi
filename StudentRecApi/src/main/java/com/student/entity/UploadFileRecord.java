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
	
	@Column(name = "upload_date")
	private Date uploadDate;
	
	@Column(name = "uploadBy")
	private String uploadBy;

	public UploadFileRecord(String fileName, Date uploadDate, String uploadBy) {
		super();
		this.fileName = fileName;
		this.uploadDate = uploadDate;
		this.uploadBy = uploadBy;
	}
	
	

}
