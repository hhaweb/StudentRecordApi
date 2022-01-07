package com.student.service;

import java.util.List;

import com.student.dto.TrainerDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;
import com.student.dto.common.SelectedItem;

public interface TrainerService {
	TrainerDto getTrainerById(Long id);
	List<TrainerDto> getTrainerListWithPager(SearchDto searchDto);
	GenericResponse saveTrainer(TrainerDto courseDto);
	GenericResponse deleteTrainer(Long id);
	List<SelectedItem> getAllTrainer();
	
}
