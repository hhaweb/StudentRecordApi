package com.student.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.dto.common.GenericResponse;
import com.student.dto.common.SelectedItem;
import com.student.entity.DropDownItem;
import com.student.repository.CommonRepository;
import com.student.service.CommonService;

@Service
public class CommonServiceImpl implements CommonService {
	@Autowired
	private CommonRepository commonRepo;

	@Override
	public List<SelectedItem> getStudentStatus() {
		List<SelectedItem> itemList = new ArrayList<SelectedItem>();
		List<String> statusList = commonRepo.getStudentStatus();
		statusList.forEach(a -> {
			if(a != null) {
				itemList.add(new SelectedItem(a, a));
			}
		});
		return itemList;
	}

	@Override
	public List<SelectedItem> getCourseLevel() {
		List<SelectedItem> itemList = new ArrayList<SelectedItem>();
		List<String> statusList = commonRepo.getCourseLevel();
		statusList.forEach(a -> {
			if(a != null) {
				itemList.add(new SelectedItem(a, a));
			}
		});
		return itemList;
	}

	@Override
	public List<SelectedItem> getDropDownItem(String name) {
		List<SelectedItem> itemList = new ArrayList<SelectedItem>();
		List<DropDownItem> dropDownList = commonRepo.findByName(name);
		dropDownList.forEach(a -> {
			if(a != null) {
				itemList.add(new SelectedItem(a.getLabel(), a.getValue()));
			}
		});
		return itemList;
	}

	@Override
	public GenericResponse saveDropDown(List<DropDownItem> dropDownItemList) {
		GenericResponse res = new GenericResponse();
		if(dropDownItemList.size() > 0 && dropDownItemList.get(0).getName() != null) {
			commonRepo.deleteByName(dropDownItemList.get(0).getName());
			commonRepo.saveAll(dropDownItemList);
			res.setStatus(true);
			res.setId(dropDownItemList.get(0).getName());
		} else {
			res = new GenericResponse(false, "Please add name");
		}
		return  res;
	}

	@Override
	public GenericResponse deleteDropDown(String name) {
		GenericResponse res = new GenericResponse();
		if(name != null) {
			commonRepo.deleteByName(name);
			res.setStatus(true);
		}
		return res;
	}

	@Override
	public List<SelectedItem> getDropDownNames() {
		List<SelectedItem> itemList = new ArrayList<SelectedItem>();
		List<String> dropDownList = commonRepo.getDropDownName();
		dropDownList.forEach(a -> {
			if(a != null) {
				itemList.add(new SelectedItem(a, a));
			}
		});
		return itemList;
	}

	
}
