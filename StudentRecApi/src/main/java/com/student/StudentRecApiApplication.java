package com.student;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class StudentRecApiApplication {
	public static void main(String[] args) {
		try {
			Date d1 = new SimpleDateFormat("MM/dd/yyyy H:mm").parse("08/18/2021  7:09");
			Date d3 = new SimpleDateFormat("d/MM/yy").parse("07/11/2021");

			Date d2 = new SimpleDateFormat("d/MM/yy").parse("11/10/00");
			String aa = "";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SpringApplication.run(StudentRecApiApplication.class, args);
	}

}
