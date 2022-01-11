package com.student.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ResourceUtils;

import com.student.dto.CourseDto;
import com.student.dto.CourseModel;
import com.student.dto.EmploymentDto;
import com.student.dto.StudentDto;

public class ExcelWriter {
	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
	
	public static void exportCourseDetail(HttpServletResponse response, CourseDto courseDto, String filePath) {
		Date date = new Date();
		String fileName = courseDto.getCourseId() + df.format(date) + ".xlsx";
		response.setContentType("application/vnd.ms-excel");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		OutputStream outputStream = null;
		try {
			String aa = filePath+File.separator+"template/CourseDetail.xlsx";
			File file = new File(filePath+File.separator+"template/CourseDetail.xlsx");
			OPCPackage pkg = OPCPackage.open(file);
			outputStream = response.getOutputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(pkg);

			XSSFSheet studentSheet = workbook.getSheetAt(0);
			Integer columnIndex = 0;

			XSSFCellStyle numberCellStyle = getCellStyle(workbook, "number", null);
			Row row = studentSheet.createRow(2);
			if(courseDto != null) {
				createCellWithString(row.createCell(columnIndex++), courseDto.getCourseId(), null);
				createCellWithString(row.createCell(columnIndex++), courseDto.getCourseName(), null);
				createCellWithString(row.createCell(columnIndex++), courseDto.getStatus(), null);
				createCellWithString(row.createCell(columnIndex++), courseDto.getSector(), null);
				createCellWithString(row.createCell(columnIndex++), courseDto.getCourseLevel(), null);
				createCellWithString(row.createCell(columnIndex++), courseDto.getDuration(), null);
				createCellWithString(row.createCell(columnIndex++), courseDto.getStartDate(), null);
				createCellWithString(row.createCell(columnIndex++), courseDto.getEndDate(), null);
				
				createCellWithNumber(row.createCell(columnIndex++), courseDto.getCohortSizeMale(), numberCellStyle);
				createCellWithNumber(row.createCell(columnIndex++), courseDto.getCohortSizeFemale(), numberCellStyle);
				createCellWithNumber(row.createCell(columnIndex++), courseDto.getNumberOfApplicantsMale(), numberCellStyle);
				createCellWithNumber(row.createCell(columnIndex++), courseDto.getNumberOfApplicantsFemale(), numberCellStyle);
				createCellWithNumber(row.createCell(columnIndex++), courseDto.getNumberOfCertifiedMale(), numberCellStyle);
				createCellWithNumber(row.createCell(columnIndex++), courseDto.getNumberOfCertifiedMale(), numberCellStyle);
				
				createCellWithString(row.createCell(columnIndex++), courseDto.getBatchNo(), null);
				createCellWithString(row.createCell(columnIndex++), courseDto.getTrainingLoaction(), null);
				String trainerId = courseDto.getTrainerId() != null ? courseDto.getTrainerId().toString() : "";
				createCellWithString(row.createCell(columnIndex++), trainerId, null);
			}

			if(courseDto.getStudentList().size() > 0) {
				columnIndex = 0;
				XSSFSheet sheet = workbook.getSheetAt(1);
				
				int courseRowIndex = 1;
				for (StudentDto student : courseDto.getStudentList()) {
					Row studentRow = sheet.createRow(courseRowIndex++);
					createCellWithString(studentRow.createCell(columnIndex++), student.getId().toString(), null);
					createCellWithString(studentRow.createCell(columnIndex++), student.getName(), null);
					createCellWithString(studentRow.createCell(columnIndex++), student.getCid().toString(), null);
					createCellWithString(studentRow.createCell(columnIndex++), student.getDid().toString(), null);
					createCellWithString(studentRow.createCell(columnIndex++), student.getEmail(), null);
					createCellWithString(studentRow.createCell(columnIndex++), student.getMobileNo(), null);
					createCellWithString(studentRow.createCell(columnIndex++), student.getDateOfBirth(), null);
					createCellWithString(studentRow.createCell(columnIndex++), student.getGender(), null);
					createCellWithNumber(studentRow.createCell(columnIndex++), student.getBatchNo(), numberCellStyle);
					columnIndex = 0;
				}
				
				
			}
			workbook.write(outputStream);
			response.flushBuffer();

			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					response.getOutputStream().flush();
					response.getOutputStream().close();
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	public static void exportStudentDetails(HttpServletResponse response, StudentDto studentDto, String filePath) {
		Date date = new Date();
		String fileName = studentDto.getName() + df.format(date) + ".xlsx";
		response.setContentType("application/vnd.ms-excel");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		OutputStream outputStream = null;
		try {
			File file = new File(filePath+File.separator+"template/StudentDetail.xlsx");

			OPCPackage pkg = OPCPackage.open(file);
			outputStream = response.getOutputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(pkg);

			XSSFSheet studentSheet = workbook.getSheetAt(0);
			Integer columnIndex = 0;
			XSSFCellStyle dateCellStype = getCellStyle(workbook, "date", null);
			XSSFCellStyle currencyCellStyle = getCellStyle(workbook, "currency", null);
			XSSFCellStyle numberCellStyle = getCellStyle(workbook, "number", null);


			Row row = studentSheet.createRow(1);
			createCellWithString(row.createCell(columnIndex++), studentDto.getId().toString(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getName().toString(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getCid(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getDid(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getDateOfBirth(), dateCellStype);
			createCellWithString(row.createCell(columnIndex++), studentDto.getEmail(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getMobileNo(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getGender(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getBloodGroup(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getMaritalStatus(), null);
			String empTypeId = studentDto.getEmploymentTypeId() == null ? ""
					: studentDto.getEmploymentTypeId().toString();
			createCellWithString(row.createCell(columnIndex++), empTypeId, null);

			createCellWithString(row.createCell(columnIndex++), studentDto.getAvatar(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getStatus(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getUserId().toString(), null);
			String trainingCenterId = studentDto.getTrainingCenterId() == null ? ""
					: studentDto.getTrainingCenterId().toString();
			createCellWithString(row.createCell(columnIndex++), trainingCenterId, null);

			createCellWithString(row.createCell(columnIndex++), studentDto.getCreatedDate(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getUpdatedDate(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getDeletedDate(), null);
			
			createCellWithNumber(row.createCell(columnIndex++), studentDto.getBatchNo(), numberCellStyle);
			
			 String trainingYear = studentDto.getTrainingYear() == null ? "" : studentDto.getTrainingYear().toString();
			createCellWithString(row.createCell(columnIndex++), trainingYear, numberCellStyle);

			if (studentDto.getEmployment() != null) {
				columnIndex = 0;
				XSSFSheet sheet = workbook.getSheetAt(1);
				Row employmentRow = sheet.createRow(1);
				EmploymentDto employment = studentDto.getEmployment();
				createCellWithString(employmentRow.createCell(columnIndex++), employment.getStatus(), null);
				createCellWithString(employmentRow.createCell(columnIndex++), employment.getOrganizationName(), null);
				createCellWithString(employmentRow.createCell(columnIndex++), employment.getOrganizationType(), null);
				createCellWithCurrency(employmentRow.createCell(columnIndex++), employment.getCurrentSalary(),
						currencyCellStyle);
				createCellWithString(employmentRow.createCell(columnIndex++), employment.getPlaceOfWork(), null);
				createCellWithString(employmentRow.createCell(columnIndex++), employment.getExpectedWorkingYear(),
						null);
				createCellWithString(employmentRow.createCell(columnIndex++), employment.getExpectedCoutinueToWork(),
						null);
				createCellWithString(employmentRow.createCell(columnIndex++), employment.getJobSpecification(), null);
				createCellWithString(employmentRow.createCell(columnIndex++), employment.getToJoinAnotherTraining(),
						null);
				createCellWithString(employmentRow.createCell(columnIndex++), employment.getDspRelatedToCurrentJob(),
						null);

			}

			if (studentDto.getCourseList().size() > 0) {
				XSSFSheet sheet = workbook.getSheetAt(2);
				columnIndex = 0;
				int courseRowIndex = 1;
				for (CourseDto course : studentDto.getCourseList()) {
					Row courseRow = sheet.createRow(courseRowIndex++);
					createCellWithString(courseRow.createCell(columnIndex++), course.getCourseId(), null);
					createCellWithString(courseRow.createCell(columnIndex++), course.getCourseName(), null);
					createCellWithString(courseRow.createCell(columnIndex++), course.getStatus(), null);
					createCellWithString(courseRow.createCell(columnIndex++), course.getSector(), null);
					createCellWithString(courseRow.createCell(columnIndex++), course.getCourseLevel(), null);
					createCellWithString(courseRow.createCell(columnIndex++), course.getDuration(), null);
					createCellWithString(courseRow.createCell(columnIndex++), course.getStartDate(), null);
					createCellWithString(courseRow.createCell(columnIndex++), course.getEndDate(), null);
					columnIndex = 0;

				}
			}

			if (studentDto.getRecommandCourses().size() > 0) {
				XSSFSheet sheet = workbook.getSheetAt(3);
				columnIndex = 0;
				int courseRowIndex = 1;
				for (CourseModel course : studentDto.getRecommandCourses()) {
					Row courseRow = sheet.createRow(courseRowIndex++);
					createCellWithString(courseRow.createCell(columnIndex++), course.getCourseId(), null);
					createCellWithString(courseRow.createCell(columnIndex++), course.getCourseName(), null);
					createCellWithString(courseRow.createCell(columnIndex++), course.getCourseLevel(), null);
					createCellWithString(courseRow.createCell(columnIndex++), course.getDuration(), null);
					createCellWithDate(courseRow.createCell(columnIndex++), course.getStartDate(), dateCellStype);
					createCellWithDate(courseRow.createCell(columnIndex++), course.getEndDate(), dateCellStype);
					columnIndex = 0;
				}
			}

			workbook.write(outputStream);
			response.flushBuffer();

			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					response.getOutputStream().flush();
					response.getOutputStream().close();
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private static void createCellWithCurrency(Cell cell, Integer value, CellStyle cellStyle) {
		if(value == null) {
			cell.setCellValue(0);
		}else {
			cell.setCellValue(value);
		}
		
		if (cellStyle != null) {
			cell.setCellStyle(cellStyle);
		}
	}

	private static void createCellWithDate(Cell cell, Date date, CellStyle cellStyle) {
		cell.setCellValue(date);
		if (cellStyle != null) {
			cell.setCellStyle(cellStyle);
		}
	}

	private static void createCellWithString(Cell cell, String value, CellStyle cellStyle) {
		cell.setCellValue(value);
		if (cellStyle != null) {
			cell.setCellStyle(cellStyle);
		}
	}
	
	private static void createCellWithNumber(Cell cell, Integer value, CellStyle cellStyle) {
		if(value == null) {
			cell.setCellValue(0);
		} else {
			cell.setCellValue(value);
		}
		if (cellStyle != null) {
			cell.setCellStyle(cellStyle);
		}
	}

	private static XSSFCellStyle getCellStyle(XSSFWorkbook workbook, String type, String fontWeight) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFDataFormat df = workbook.createDataFormat();

		// Type
		if (type != null) {
			if (type.equalsIgnoreCase("date")) {
				cellStyle.setDataFormat(df.getFormat("mm/dd/yyyy"));
			} else if (type.equalsIgnoreCase("currency")) {
				cellStyle.setDataFormat(df.getFormat("#,##0.00"));
			} else if (type.equalsIgnoreCase("number")) {
				cellStyle.setDataFormat(df.getFormat("#,##0"));
			}
		}

		// font weight
		if (fontWeight != null && fontWeight.equalsIgnoreCase("bold")) {
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			cellStyle.setFont(font);
		}
		return cellStyle;
	}
}
