package com.student.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.student.config.ERole;
import com.student.dto.StudentDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;
import com.student.entity.Role;
import com.student.entity.Student;
import com.student.entity.User;
import com.student.repository.RoleRepository;
import com.student.repository.StudentRepository;
import com.student.service.StudentService;
import com.student.util.CSVHelper;
import com.student.util.CommonUtil;

@Service
public class StudentServiceImpl implements StudentService {
	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private RoleRepository roleRepo;

	@Override
	public StudentDto getStudentById(Long id) {
		Student student = studentRepo.findById(id).orElse(null);
		if (student == null) {
			return null;
		}
		StudentDto studentDto = new StudentDto(student);
		return studentDto;
	}

	@Override
	public List<StudentDto> getStudentWithPager(SearchDto searchDto) {
		List<StudentDto> studentDtoList = new ArrayList<StudentDto>();

		int pageNo = searchDto.getRowOffset();
		int pageSize = (int) (searchDto.isExport == true ? studentRepo.getTotalRecord() : searchDto.getRowsPerPage());
		String sortBy = searchDto.getSortName();
		Pageable paging = PageRequest.of(pageNo, pageSize,
				searchDto.getSortType() == 1 ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
		List<Student> studentList = new ArrayList<Student>();
		if (searchDto.getSearchKeyword() != null && CSVHelper.isNumeric(searchDto.getSearchKeyword())) {
			Long id = Long.parseLong(searchDto.getSearchKeyword());
			Student student = studentRepo.findById(id).orElse(null);
			studentList.add(student);
		} else {
			studentList = studentRepo.findAll(paging).toList();
		}

		if (studentList.size() > 0) {
			for (Student student : studentList) {
				studentDtoList.add(new StudentDto(student));
			}
			studentDtoList.get(0).setTotalRecord(studentRepo.getTotalRecord());
		}

		return studentDtoList;
	}

	private User getUser(String userName, String email) {
		Role studentRole = roleRepo.findByName(ERole.ROLE_STUDENT).orElse(null);
		String password = commonUtil.generatePassword();
		String userEmail = null;
		if(email != null && !email.isEmpty()) {
			userEmail = email;
		}
		User user = new User(userName, userEmail, encoder.encode(password), new Date(), new Date(), studentRole);
		return user;
	}

	@Override
	public GenericResponse saveStudent(StudentDto studentDto) {

		if (studentDto.getName() == null || studentDto.getName().isEmpty()) {
			return new GenericResponse(false, "Name is empty");
		}

		if (studentDto.getCid() == null || studentDto.getCid().isEmpty()) {
			return new GenericResponse(false, "CID is empty");
		}

		if (studentDto.getDid() == null || studentDto.getDid().isEmpty()) {
			return new GenericResponse(false, "Did is empty");
		}

		if (studentRepo.isExistCidNumberById(studentDto.getCid(), studentDto.getId())) {
			return new GenericResponse(false, "Cid is already exist");
		}

		if (studentRepo.isExistDidNumberById(studentDto.getDid(), studentDto.getId())) {
			return new GenericResponse(false, "Did is already exist");
		}

		Student saveObj = null;
		try {
			Student student = studentDto.getEntity();
			if (student.getId() == null || student.getId() == 0) {
				String userName = studentDto.getName() + "-" + studentDto.getCid();
				User user = getUser(userName, student.getEmail());
				student.setUser(user);
			}
			saveObj = studentRepo.saveAndFlush(student);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new GenericResponse(saveObj.getId().toString(), true, "Save Succesfully");
	}

	@Override
	public StudentDto getStudentByUserId(Long userId) {
		StudentDto studentDto = null;
		Student student = studentRepo.findByUserId(userId).orElse(null);
		if(student!= null) {
			studentDto = new StudentDto(student);
		}
		return studentDto;
	}

}
