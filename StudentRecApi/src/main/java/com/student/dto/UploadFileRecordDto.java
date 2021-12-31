package com.student.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.student.config.ConfigData;
import com.student.entity.UploadFileRecord;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UploadFileRecordDto {
	
	private Long id;
	private String fileName;
	private String errorFileName;
	private int totalRecord;
	private int successRecord;
	private int failRecord;
	private String uploadDate;
	private String uploadBy;
	
	public UploadFileRecordDto(UploadFileRecord upload) {
		DateFormat df = new SimpleDateFormat(ConfigData.DateFormat);
		this.id = upload.getId();
		this.fileName = upload.getFileName();
		this.errorFileName = upload.getErrorFileName();
		this.totalRecord = upload.getTotalRecord();
		this.successRecord = upload.getSuccessRecord();
		this.failRecord = upload.getFailRecord();
		this.uploadDate = upload.getUploadDate() != null ? df.format(upload.getUploadDate()) : null;
		this.uploadBy = upload.getUploadBy();
	}
	
	
}
