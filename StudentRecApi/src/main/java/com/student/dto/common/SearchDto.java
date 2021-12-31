package com.student.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchDto {
	public int rowOffset;
	public int rowsPerPage;
	public String sortName;
	public int sortType;
	public String searchKeyword;
	public boolean isExport;
}
