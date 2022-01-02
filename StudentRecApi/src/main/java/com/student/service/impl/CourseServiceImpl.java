package com.student.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.student.config.ResponseMessage;
import com.student.dto.CourseDto;
import com.student.dto.CourseModel;
import com.student.dto.TrainerDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;
import com.student.entity.Course;
import com.student.entity.Trainer;
import com.student.repository.CourseRepository;
import com.student.repository.StudentRepository;
import com.student.repository.TrainerRepository;
import com.student.service.CourseService;
import com.student.util.CSVHelper;

@Service
public class CourseServiceImpl implements CourseService {
	@Autowired
	private CourseRepository courseRepo;
	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private TrainerRepository trainerRepo;

	@Override
	public CourseDto getCourseById(Long id) {
		Course course = courseRepo.findById(id).orElse(null);
		CourseDto courseDto = null;
		if (course != null) {
			courseDto = new CourseDto(course);
		}
		return courseDto;
	}

	@Override
	public GenericResponse saveCourse(CourseDto courseDto) {
		Course saveObj = null;
		try {
			Course course = courseDto.getEntity();
			saveObj = courseRepo.saveAndFlush(course);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new GenericResponse(saveObj.getId().toString(), true, ResponseMessage.SAVE_SUCCESS);
	}

	@Override
	public List<CourseModel> getCourseListWithPager(SearchDto searchDto) {

		int pageNo = searchDto.getRowOffset();
		int pageSize = searchDto.getRowsPerPage();
		String sortBy = searchDto.getSortName();
		Pageable paging = PageRequest.of(pageNo, pageSize,
				searchDto.getSortType() == 1 ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
		List<CourseModel> courseList = new ArrayList<CourseModel>();
		if (searchDto.getSearchKeyword() != null && !searchDto.getSearchKeyword().isEmpty()) {
			courseList = courseRepo.getCourseByPager(searchDto.searchKeyword, searchDto.searchKeyword, paging);
		} else {
			courseList = courseRepo.getCourseByPagerWithoutFilter(paging);
		}
		
		if(courseList.size() > 0) {
			Long totalRecords = courseRepo.getTotalRecordGroupBy(searchDto.searchKeyword, searchDto.searchKeyword);
			courseList.get(0).setTotalRecords(totalRecords);
		}
		return courseList;
	}

	@Override
	public List<CourseDto> getCourseByCid(String cId) {
		List<CourseDto> courseDtoList = new ArrayList<CourseDto>();
		List<Course> courseList = courseRepo.findBycId(cId);
		if(courseList != null && courseList.size() > 0) {
			for (Course course : courseList) {
				courseDtoList.add(new CourseDto(course));
			}
		}
		return courseDtoList;
	}

	@Override
	public List<CourseModel> getRecommendedCourses(String cid) {
		List<Course> courseList = courseRepo.findBycId(cid);
		List<String> courseIdList = new ArrayList<String>();
		List<String> sectorList = new ArrayList<String>();
		for (Course course : courseList) {
			courseIdList.add(course.getCourseId());
			sectorList.add(course.getSector());
		}
		
		List<CourseModel> recommendedCourses = courseRepo.getRecommendCourses(courseIdList,sectorList) ;
		return recommendedCourses;
	}

}
