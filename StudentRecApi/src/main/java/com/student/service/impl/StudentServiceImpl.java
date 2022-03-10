package com.student.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.student.config.ERole;
import com.student.config.ResponseMessage;
import com.student.dto.EmploymentDto;
import com.student.dto.StudentDto;
import com.student.dto.common.GenericResponse;
import com.student.dto.common.SearchDto;
import com.student.entity.Course;
import com.student.entity.Employment;
import com.student.entity.Role;
import com.student.entity.Student;
import com.student.entity.User;
import com.student.repository.CourseRepository;
import com.student.repository.EmploymentRepository;
import com.student.repository.RoleRepository;
import com.student.repository.StudentRepository;
import com.student.repository.UserRepository;
import com.student.service.StudentService;
import com.student.util.CSVHelper;

@Service
public class StudentServiceImpl implements StudentService {
	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private EmploymentRepository employmentRepo;

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private CourseRepository courseRepo;

	@Autowired
	PasswordEncoder encoder;

	@Override
	public StudentDto getStudentById(Long id) {
		Student student = studentRepo.findById(id).orElse(null);
		if (student == null) {
			return null;
		} else {
			StudentDto studentDto = new StudentDto(student);
			Employment employment = employmentRepo.findByCid(student.getCid()).orElse(null);
			if (employment != null) {
				EmploymentDto employmentDto = new EmploymentDto(employment);
				studentDto.setEmployment(employmentDto);
			}
			return studentDto;
		}

	}

	@Override
	public List<StudentDto> getStudentWithPager(SearchDto searchDto) {
		List<StudentDto> studentDtoList = new ArrayList<StudentDto>();
		Long totalRecord = (long) 0;
		Long id = null;
		if (CSVHelper.isNumeric(searchDto.getSearchKeyword())) {
			id = Long.parseLong(searchDto.getSearchKeyword());
		}
		if (searchDto.getSearchKeyword() != null) {
			totalRecord = studentRepo.getTotalRecordWithFilter(id, searchDto.getSearchKeyword(),
					searchDto.getSearchKeyword());
		} else {
			totalRecord = studentRepo.getTotalRecord();
		}
		int pageNo = searchDto.getRowOffset();
		int pageSize = (int) (searchDto.isExport == true ? totalRecord : searchDto.getRowsPerPage());
		String sortBy = searchDto.getSortName();
		Pageable paging = PageRequest.of(pageNo, pageSize,
				searchDto.getSortType() == 1 ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
		List<Student> studentList = new ArrayList<Student>();

		if (searchDto.getSearchKeyword() != null) {
			if (CSVHelper.isNumeric(searchDto.getSearchKeyword())) {
				id = Long.parseLong(searchDto.getSearchKeyword());
			}
			studentList = studentRepo.getStudentByPager(id, searchDto.getSearchKeyword(), searchDto.getSearchKeyword(),
					paging);
		} else {
			studentList = studentRepo.findAll(paging).toList();
		}

		if (studentList.size() > 0) {
			for (Student student : studentList) {
				studentDtoList.add(new StudentDto(student));
			}
			studentDtoList.get(0).setTotalRecord(totalRecord);
		}

		return studentDtoList;
	}

	// crate user with student role
	private void saveUser(String userName, String password) {
		Role studentRole = roleRepo.findByName(ERole.ROLE_STUDENT).orElse(null);
		String userEmail = null;
		User user = new User(userName, null	, password, new Date(), new Date(), studentRole);
		user.setFirstTimeLogin(true);
		userRepo.save(user);
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

		if (studentDto.getId() != null && studentDto.getId() != 0) {
			if (studentRepo.isExistCidNumberById(studentDto.getCid(), studentDto.getId())) {
				return new GenericResponse(false, "Cid is already exist");
			}

			if (studentRepo.isExistDidNumberById(studentDto.getDid(), studentDto.getId())) {
				return new GenericResponse(false, "Did is already exist");
			}
		} else {
			if (studentRepo.isExistCidNumber(studentDto.getCid())) {
				return new GenericResponse(false, "Cid is already exist");
			}

			if (studentRepo.isExistDidNumber(studentDto.getDid())) {
				return new GenericResponse(false, "Did is already exist");
			}
		}

		Student saveObj = null;
		try {
			Student student = null;
			try {
				student = studentDto.getEntity();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (student.getId() == null || student.getId() == 0) {
//				Long maxId = studentRepo.getStudentMaxId().orElse((long) 0);
//				student.setId(maxId + 1); // set student id
				String userName =  studentDto.getCid();
				String password = studentDto.getDid().trim().substring(6);//
				if (!userRepo.existsByUserName(userName)) {
					saveUser(userName, password);
				}
			}
			EmploymentDto employmentDto = studentDto.getEmployment();
			if (employmentDto != null) {
				employmentDto.setCid(studentDto.getCid());
			}
			if (employmentDto != null) {
				employmentDto.setCid(studentDto.getCid());
				Employment employment = employmentDto.getEntity();
				employmentRepo.save(employment);
			}
			saveObj = studentRepo.saveAndFlush(student);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new GenericResponse(saveObj.getId().toString(), true, "Save Succesfully");
	}

	@Override
	public StudentDto getStudentByCid(String cid) {
		StudentDto studentDto = null;
		Student student = studentRepo.findByCid(cid).orElse(null);
		if (student != null) {
			studentDto = new StudentDto(student);
		}
		return studentDto;
	}

	@Override
	public List<StudentDto> getStudentByCourseId(Long id) { // id from course table not courseId
		// TODO Auto-generated method stub
		List<StudentDto> studentDtoList = new ArrayList<StudentDto>();
		List<String> cidList = new ArrayList<String>();
		Course course = courseRepo.findById(id).orElse(null);

		if (course != null) {
			List<Course> courseList = courseRepo.findByCourseIdAndCourseNameAndBatchNoAndTrainingLoaction(
					course.getCourseId(), course.getCourseName(), course.getBatchNo(), course.getTrainingLoaction());
			courseList.forEach(a -> cidList.add(a.getCId()));
			List<Student> studentList = studentRepo.findByCidIn(cidList);
			if (studentList != null && studentList.size() > 0) {
				for (Student student : studentList) {
					studentDtoList.add(new StudentDto(student));
				}
			}
		}

		return studentDtoList;
	}

	@Override
	public GenericResponse deleteStudent(Long studentId) {
		Student student = studentRepo.findById(studentId).orElse(null);
		if (student != null) {
			String userName = student.getCid();
			User user = userRepo.findByUserName(userName).orElse(null);
			if (user != null) {
				userRepo.delete(user);
			}
			courseRepo.deleteBycId(student.getCid());
			studentRepo.delete(student);
		}
		return new GenericResponse(true, ResponseMessage.DELETE_SUCCESS);
	}

	@Override
	public GenericResponse deleteStudents(List<StudentDto> studentDtoList) {
		for (StudentDto studentDto : studentDtoList) {
			Student student = studentRepo.findById(studentDto.getId()).orElse(null);
			if (student != null) {
				String userName = student.getCid();
				User user = userRepo.findByUserName(userName).orElse(null);
				if (user != null) {
					userRepo.delete(user);
				}
				courseRepo.deleteBycId(student.getCid());
				studentRepo.delete(student);
			}
		}
		return new GenericResponse(true, ResponseMessage.DELETE_SUCCESS);
	}

}
