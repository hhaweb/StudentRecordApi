package com.student.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.student.dto.StudentDto;
import com.student.entity.Student;

@Component
public class CSVHelper {
	private static final Logger logger = LoggerFactory.getLogger(CSVHelper.class);
	


	public static String TYPE = "text/csv";
	
	public static Boolean saveFile(MultipartFile file, String fileName, String uploadPath) {
		try {
			Path root = Paths.get(uploadPath);
			if (!Files.exists(root)) {
			    try {
		            Files.createDirectories(Paths.get(uploadPath));
		        } catch (IOException e) {
		        	return false;
		        }
			}
			
			Files.copy(file.getInputStream(), root.resolve(fileName+".csv"));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean hasCSVFormat(MultipartFile file) {
		if (TYPE.equals(file.getContentType()) || file.getContentType().equals("application/vnd.ms-excel")) {
			return true;
		}

		return false;
	}

	public static List<StudentDto> readStudentCSV(String path) {
		List<StudentDto> studentDtoList = new ArrayList<StudentDto>(); // Default to empty list.
		try {
			studentDtoList = new CsvToBeanBuilder<StudentDto>(new FileReader(path)).withType(StudentDto.class)

					.withIgnoreLeadingWhiteSpace(true).build().parse();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return studentDtoList;
	}
	
	
	
	
	public static boolean validateCSVFile(MultipartFile file, List<String> Header) {
				
		if (hasCSVFormat(file)) {
			try {
				CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));	
				String [] headerList = reader.readNext();
				if(headerList != null) {
					if (Arrays.asList(headerList).containsAll(Header)){
					   return true;
					}		
				}
			}catch(Exception ex) {
				return false;
			}
		}
		return false;	
	}

	

}
