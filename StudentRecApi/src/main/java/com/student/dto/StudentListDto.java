package com.student.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentListDto {
	
	private Long studentId;
	
	private int courseId;
	
	private int rowOffset;
	
	private int rowsPerPage;
	
	private String sortName;
	
	private int sortType;

}
