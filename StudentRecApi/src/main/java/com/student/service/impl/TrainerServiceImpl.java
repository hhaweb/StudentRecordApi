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
import com.student.dto.common.SelectedItem;
import com.student.entity.Trainer;
import com.student.repository.CourseRepository;
import com.student.repository.TrainerRepository;
import com.student.service.TrainerService;
import com.student.util.CSVHelper;

@Service
public class TrainerServiceImpl implements TrainerService{
	@Autowired
	private TrainerRepository trainerRepo;
	@Autowired
	private CourseRepository courseRepo;
	

	@Override
	public TrainerDto getTrainerById(Long id) {
		Trainer trainer = trainerRepo.findById(id).orElse(null);
		TrainerDto trainerDto = new TrainerDto(trainer);
		return trainerDto;
	}

	@Override
	public List<TrainerDto> getTrainerListWithPager(SearchDto searchDto) {
		List<TrainerDto> trainerDtoList = new ArrayList<TrainerDto>();
		Long totalRecord = (long) 0;
		if(searchDto.searchKeyword != null) {
			totalRecord = trainerRepo.getTotalRecordWithFilter(searchDto.searchKeyword, searchDto.searchKeyword);
		} else {
			totalRecord = trainerRepo.getTotalRecord();
		}
		
		int pageNo = searchDto.getRowOffset();
		int pageSize = (int) (searchDto.isExport == true ? totalRecord : searchDto.getRowsPerPage());
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
			trainerDtoList.get(0).setTotalRecords(totalRecord);
		}
		
		return trainerDtoList;
	}

	@Override
	public GenericResponse saveTrainer(TrainerDto trainerDto) {
		GenericResponse response = new GenericResponse();
		if(trainerDto.getTrainerId() == null || trainerDto.getTrainerId().isEmpty()) {
			return new GenericResponse(false, "Trainer Id is empty");
		}
		if(trainerDto.getTrainerName() == null || trainerDto.getTrainerName().isEmpty()) {
			return new GenericResponse(false, "Trainer name is empty");
		}
		
		if(trainerDto.getId() != null && trainerDto.getId() != 0) {
			if(trainerRepo.isExistTrainerIdById(trainerDto.getTrainerId(), trainerDto.getId())) {
				return new GenericResponse(false, "Trainer Id is already exist");
			}
		} else {
			if(trainerRepo.isExistTrainerId(trainerDto.getTrainerId())) {
				return new GenericResponse(false, "Trainer Id is already exist");
			}
		}
		
		Trainer saveObj = null;
		try {
			Trainer	trainer = trainerDto.getEntity();
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
			courseRepo.updateTrainerIdFromCourse(trainer.getTrainerId());
			trainerRepo.delete(trainer);
			response.setStatus(true);
			response.setMessage("Delete Successfully");
		} else {
			response.setStatus(false);
			response.setMessage("Trainer does not exist");
		}
		return response;
	}

	@Override
	public List<SelectedItem> getAllTrainer() {
		List<SelectedItem> itemList = new ArrayList<SelectedItem>();
		trainerRepo.findAll().forEach(t -> {
			if(t != null) {
				itemList.add(new SelectedItem(t.getTrainerName(), t.getTrainerId(), false));
			}
		});
		return itemList;
	}

	@Override
	public GenericResponse deleteTrainers(List<TrainerDto> trainerDtoList) {
		GenericResponse response = new GenericResponse();
		for (TrainerDto trainerDto : trainerDtoList) {
			Trainer trainer = trainerRepo.findById(trainerDto.getId()).orElse(null);
			
			if(trainer != null) {
				courseRepo.updateTrainerIdFromCourse(trainer.getTrainerId());
				trainerRepo.delete(trainer);
				response.setStatus(true);
				response.setMessage("Delete Successfully");
			} else {
				response.setStatus(false);
				response.setMessage("Trainer does not exist");
			}
		}
		return response;
	}

}
