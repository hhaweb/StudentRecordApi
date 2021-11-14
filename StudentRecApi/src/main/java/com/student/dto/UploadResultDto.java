package com.student.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UploadResultDto {
	private int id;
	private int successCount;
	private int failCount;
	private int totalCount;
	public UploadResultDto(int id, int successCount, int failCount, int totalCount) {
		super();
		this.id = id;
		this.successCount = successCount;
		this.failCount = failCount;
		this.totalCount = totalCount;
	}

	
	
}
