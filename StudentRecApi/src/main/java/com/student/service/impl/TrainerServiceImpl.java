package com.student.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.student.dto.TrainerDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;
import com.student.entity.Trainer;
import com.student.repository.TrainerRepository;
import com.student.service.TrainerService;
import com.student.util.CSVHelper;

@Service
public class TrainerServiceImpl implements TrainerService{
	@Autowired
	private TrainerRepository trainerRepo;
	

	@Override
	public TrainerDto getTrainerById(Long id) {
		Trainer trainer = trainerRepo.findById(id).orElse(null);
		TrainerDto trainerDto = new TrainerDto(trainer);
		return trainerDto;
	}

	@Override
	public List<TrainerDto> getTrainerListWithPager(SearchDto searchDto) {
		List<TrainerDto> trainerDtoList = new ArrayList<TrainerDto>();
		int pageNo = searchDto.getRowOffset();
		int pageSize = searchDto.getRowsPerPage();
		String sortBy = searchDto.getSortName();
		Pageable paging = PageRequest.of(pageNo, pageSize,
				searchDto.getSortType() == 1 ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
		List<Trainer> trainerList = new ArrayList<Trainer>();
		if (searchDto.getSearchKeyword() != null && !searchDto.getSearchKeyword().isEmpty()) {
				
			 trainerList = trainerRepo.getTrainerByPager(searchDto.getSearchKeyword(),searchDto.getSearchKeyword() , paging);
		} else {
			trainerList = trainerRepo.findAll(paging).toList();
		}

		if (trainerList != null && trainerList.size() > 0) {
			for (Trainer trainer : trainerList) {
				trainerDtoList.add(new TrainerDto(trainer));
			}
			trainerDtoList.get(0).setTotalRecords(trainerRepo.getTotalRecord());;
		}
		
		return trainerDtoList;
	}

	@Override
	public GenericResponse saveTrainer(TrainerDto courseDto) {
		GenericResponse response = new GenericResponse();
		if(courseDto.getTrainerId() == null || courseDto.getTrainerId().isEmpty()) {
			return new GenericResponse(false, "Trainer Id is empty");
		}
		if(courseDto.getTrainerName() == null || courseDto.getTrainerName().isEmpty()) {
			return new GenericResponse(false, "Trainer name is empty");
		}
		
		Trainer saveObj = null;
		try {
			Trainer	trainer = courseDto.getEntity();
			saveObj = trainerRepo.saveAndFlush(trainer);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setStatus(true);
		response.setId(saveObj.getId().toString());
		response.setMessage("Save Successfully");
		return response;
	}

	@Override
	public GenericResponse deleteTrainer(Long id) {
		GenericResponse response = new GenericResponse();
		Trainer trainer = trainerRepo.findById(id).orElse(null);
		if(trainer != null) {
			trainerRepo.delete(trainer);
			response.setStatus(true);
			response.setMessage("Delete Successfully");
		} else {
			response.setMessage("Trainer does not exist");
		}
		return response;
	}

}
