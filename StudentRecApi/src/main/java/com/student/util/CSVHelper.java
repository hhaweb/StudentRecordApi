package com.student.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.student.config.ConfigData;
import com.student.dto.csv.StudentCsvDto;
import com.student.entity.Student;

@Component
public class CSVHelper {
	private static final Logger logger = LoggerFactory.getLogger(CSVHelper.class);
	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
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

			Files.copy(file.getInputStream(), root.resolve(fileName + ".csv"));
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

	public static List<StudentCsvDto> readStudentCSV(String path) {
		List<StudentCsvDto> studentDtoList = new ArrayList<StudentCsvDto>(); // Default to empty list.
		try {
			studentDtoList = new CsvToBeanBuilder<StudentCsvDto>(new FileReader(path)).withType(StudentCsvDto.class)

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
				String[] headerList = reader.readNext();
				if (headerList != null) {
					if (Arrays.asList(headerList).containsAll(Header)) {
						return true;
					}
				}
			} catch (Exception ex) {
				return false;
			}
		}
		return false;
	}

	public static boolean validateDateFormat(String dateStr, String format) {
		DateFormat df = new SimpleDateFormat(format);
		try {
			Date d = df.parse(dateStr);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isNumeric(String number) {
		int intValue = 0;
		try {
			intValue = Integer.parseInt(number);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static <T> boolean isHaveDuplicate(List<T> list) {

		// Set to store the duplicate elements
		Set<T> items = new HashSet<>();
		items = list.stream().filter(i -> Collections.frequency(list, i) > 1).collect(Collectors.toSet());
		int dd = items.size();
		return items.size() > 0 ? true : false;
	}

	public static void exportStudentLisr(HttpServletResponse response, List<StudentCsvDto> studentList) {
		Date date = new Date();
		String fileName = "student_"+df.format(date)+".csv";
		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
		response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		List<String> headerList = ConfigData.StudentErrorCSVHeader;
		CSVWriter csvWriter;
		try {
			csvWriter = new CSVWriter(response.getWriter());
			String[] headers = (String[]) headerList.toArray();
			csvWriter.writeNext(headers); // write header
			for (StudentCsvDto student : studentList) {
				csvWriter.writeNext(new String[] { student.getId(), student.getName(), student.getCid(),
						student.getDid(), student.getDateOfBirth(), student.getEmail(), student.getMobileNo(),
						student.getGender(), student.getBloodGroup(), student.getMartialStatus(),
						student.getEmploymentTypeId(), student.getAvatar(), student.getStatus(), student.getUserId(),
						student.getTrainingCenterId(), student.getCreatedAt(), student.getUpdatedAt(),
						student.getDeletedAt(), student.getBatchNo(), student.getTrainingYear(),
						student.getErrorMessage() });
			}
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
	}
}
