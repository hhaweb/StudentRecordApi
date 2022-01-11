package com.student.dto;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UploadResultDto {

	private int successCount;
	private int failCount;
	private int totalCount;
	private String uploadType;
//	private List<StudentCsvDto> studentErrorList;
//	private List<CourseCsvDto> courseErrorList;
//	private List<TrainerCsvDto> trainerErrorList;
	
}
