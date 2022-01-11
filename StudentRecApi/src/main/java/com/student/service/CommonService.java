package com.student.service;

import java.util.List;

import com.student.dto.common.GenericResponse;
import com.student.dto.common.SelectedItem;
import com.student.entity.DropDownItem;

public interface CommonService {

	List<SelectedItem> getStudentStatus();
	List<SelectedItem> getCourseLevel();
	List<SelectedItem> getDropDownItem(String name);
	List<SelectedItem> getDropDownNames();
	GenericResponse saveDropDown(List<DropDownItem> dropDownItemLsit);
	GenericResponse deleteDropDown(String name);
	
}
