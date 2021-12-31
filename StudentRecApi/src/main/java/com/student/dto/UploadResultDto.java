package com.student.dto;

import java.util.List;

import com.student.dto.csv.CourseCsvDto;
import com.student.dto.csv.StudentCsvDto;
import com.student.dto.csv.TrainerCsvDto;

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
	private List<StudentCsvDto> studentErrorList;
	private List<CourseCsvDto> courseErrorList;
	private List<TrainerCsvDto> trainerErrorList;
	
}
