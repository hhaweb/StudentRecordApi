package com.student.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.student.config.ERole;
import com.student.dto.NewStudentDto;
import com.student.dto.StudentListDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.ResponseMessage;
import com.student.entity.Role;
import com.student.entity.Student;
import com.student.repository.RoleRepository;
import com.student.repository.StudentListRepo;
import com.student.repository.StudentRepository;
import com.student.repository.UserRepository;
import com.student.service.StudentService;
import com.student.service.UploadService;

@Service
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	private StudentRepository studentRepo;
	
	@Autowired
	private StudentListRepo studentListRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UploadService uploadService;
	
	
	public GenericResponse saveStudent(NewStudentDto studentDto) {
		Role studentRole = roleRepo.findByName(ERole.ROLE_STUDENT).orElse(null);
		Set<Role> roles = new HashSet<>();
		roles.add(studentRole);
		Long userId = uploadService.createUser(studentDto.getName(),roles);
		Student studentObj = studentDto.getEntity();
		studentObj.setUserId(userId);
		studentRepo.save(studentObj);
		return  new GenericResponse(true, ResponseMessage.SAVE_SUCCESS);
	   
	}


	@Override
	public GenericResponse getAllStudents(StudentListDto studentListDto) {

		Pageable paging = PageRequest.of(studentListDto.getRowOffset(), studentListDto.getRowsPerPage(), Sort.by(studentListDto.getSortName()).descending());
		Page<Student> studentResults = studentListRepo.findAll(paging);
		long totalStudents =studentResults.getTotalElements();
		Map<String,Object> results = new HashMap<String,Object>();
		results.put("students", studentResults.getContent());
		results.put("totalStudents", totalStudents);
		return new GenericResponse(true,results);
	}
}
