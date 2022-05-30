package com.student;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.student.dto.StudentDto;
import com.student.entity.Student;
import com.student.repository.StudentRepository;
import com.student.service.StudentService;
import com.student.service.impl.StudentServiceImpl;


@SpringBootTest
public class StudentRecApiApplicationTests {
	
	@TestConfiguration
	static class StudentServieImplTestContextConfiguration {
		@Bean
		public StudentService studentService() {
			return new StudentServiceImpl();
		}
	}

	@Autowired
	private StudentService studentService;

	@MockBean
	private StudentRepository studentRepository;
	
	@BeforeEach
	public void setUp() {
		Long id = (long) 18;
		Optional<Student> s = Optional.ofNullable(new Student());
		s.get().setId(id);
	    Mockito.when(   studentRepository.findById(id) ).thenReturn( s);
	     
	}
	
	@Test
	public void whenValidName_thenEmployeeShouldBeFound() {
	  Long id = (long) 18;
	  StudentDto found = studentService.getStudentById(id);
	  assertThat(found.getId()).isEqualTo(id);
	 }
}
