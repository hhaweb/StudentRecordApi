package com.student.dto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.student.config.ConfigData;
import com.student.entity.UploadHistory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UploadHistoryDto {
	
	private Long id;
	private String fileName;
	private String errorFileName;
	private int totalRecord;
	private int successRecord;
	private int failRecord;
	private String uploadDate;
	private String uploadType;
	private String uploadBy;
	
	public UploadHistoryDto(UploadHistory upload) {
		DateFormat df = new SimpleDateFormat(ConfigData.DateFormat);
		this.id = upload.getId();
		this.fileName = upload.getFileName();
		this.uploadType = upload.getUploadType();
		 int dotIndex = upload.getFileName().lastIndexOf('.');
		 String fileName = (dotIndex == -1) ? upload.getFileName() : upload.getFileName().substring(0, dotIndex);
		 
		this.errorFileName = upload.getFailRecord() > 0 ? fileName+ConfigData.errorFileName : null;
		this.totalRecord = upload.getTotalRecord();
		this.successRecord = upload.getSuccessRecord();
		this.failRecord = upload.getFailRecord();
		this.uploadDate = upload.getUploadDate() != null ? df.format(upload.getUploadDate()) : null;
		this.uploadBy = upload.getUploadBy();
	}
	
	
}
