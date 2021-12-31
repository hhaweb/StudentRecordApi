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
		SpringApplication.run(StudentRecApiApplication.class, args);
	
//		DateFormat df = new SimpleDateFormat("d/MM/yyyy H:mm");
//		String date = "7/29/2021 11:53";
//		try {
//			Date d = df.parse(date);
//			System.out.print(d);
//			
//			
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
	}

}
