package com.student.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
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
import java.util.Base64;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.student.config.ConfigData;
import com.student.dto.csv.CourseCsvDto;
import com.student.dto.csv.StudentCsvDto;
import com.student.dto.csv.TrainerCsvDto;
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

	public static boolean validateCSVFile(MultipartFile file, String[] Header) {

		if (hasCSVFormat(file)) {
			try {
				CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));
				String[] headerList = reader.readNext();
				if (headerList != null) {
					if (Arrays.asList(headerList).containsAll(Arrays.asList(Header))) {
						return true;
					}
				}
			} catch (Exception ex) {
				return false;
			}
		}
		return false;
	}
	
	public static boolean isNumeric(String number) {
		Long intValue = (long) 0;
		try {
			intValue = Long.parseLong(number);
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

	public static void exportStudentList(HttpServletResponse response, List<StudentCsvDto> studentList) {
		Date date = new Date();
		String fileName = "student_" + df.format(date) + ".csv";
		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		String[] headers = ConfigData.StudentCSVHeaderError;
		CSVWriter csvWriter;
		try {
			csvWriter = new CSVWriter(response.getWriter());
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

	public static void exportCourseList(HttpServletResponse response, List<CourseCsvDto> courseList) {
		Date date = new Date();
		String fileName = "course_" + df.format(date) + ".csv";
		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		String[] headers = ConfigData.CourseCSVExportHeader;
		CSVWriter csvWriter;
		try {
			csvWriter = new CSVWriter(response.getWriter());
			csvWriter.writeNext(headers); // write header
			for (CourseCsvDto course : courseList) {
				csvWriter.writeNext(new String[] { course.getCourseId(), course.getCourseName(), course.getStatus(),
						course.getCourseLevel(), course.getStartDate(), course.getEndDate(), course.getCohortSizeMale(),
						course.getCohortSizeFemale(), course.getBatchNo(), course.getTrainingLoaction(), });
			}
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void exportTrainerList(HttpServletResponse response, List<TrainerCsvDto> trainerList) {
		Date date = new Date();
		String fileName = "trainer_" + df.format(date) + ".csv";
		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		String[] headers = ConfigData.TrainerCSVHeader;
		CSVWriter csvWriter;
		try {
			csvWriter = new CSVWriter(response.getWriter());
			csvWriter.writeNext(headers); // write header
			for (TrainerCsvDto trainer : trainerList) {
				csvWriter.writeNext(new String[] { trainer.getTrainerId(), trainer.getTrainerName(),
						trainer.getGender(), trainer.getNationality(), trainer.getJoinDate(), trainer.getDesignation(),
						trainer.getDepartment(), trainer.getBranch(), trainer.getDspCenter(),
						trainer.getTrainingProgramme(), trainer.getAffiliation(), trainer.getQualification() });
			}
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public static String convertByteArrayToBase64String(byte[] byteArr) {
		String base64String = new String(Base64Utils.decode(byteArr));
		return base64String;

	}

	public static byte[] convertBase64ToByteArray(String base64) throws UnsupportedEncodingException {
		byte[] byteArr = Base64Utils.encode(base64.getBytes());
		return byteArr;

	}
}
