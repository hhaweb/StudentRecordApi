package com.student.util;
import java.io.File;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ResourceUtils;

import com.student.dto.StudentDto;
public class ExcelWriter {
	static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
	
	
	
	
	public static void exportStudentDetails(HttpServletResponse response,StudentDto studentDto) {
		Date date = new Date();
		String fileName = studentDto.getName()+df.format(date)+".xlsx";
		response.setContentType("application/vnd.ms-excel");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
		response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		OutputStream outputStream = null;
		try {
			File file = ResourceUtils.getFile("classpath:Template/SaleHeaderList.xlsx");
			
			OPCPackage pkg = OPCPackage.open(file);
			outputStream = response.getOutputStream();
			XSSFWorkbook workbook = new XSSFWorkbook(pkg);
			
			XSSFSheet  sheet = workbook.getSheetAt(0);	
			workbook.setSheetName(0, "Student Information");
			workbook.setSheetName(1, "Course Information");
			workbook.setSheetName(2, "Recommand Course Information");
			Integer rowIndex = 2;
			Integer columnIndex = 0;
			
			XSSFCellStyle dateCellStype = getCellStyle(workbook, "date",null);
			XSSFCellStyle boldCellStype = getCellStyle(workbook, null,"bold");
			
			Row row = sheet.createRow(2);
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
			createCellWithString(row.createCell(columnIndex++), studentDto.getEmploymentTypeId().toString(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getAvatar(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getStatus(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getUserId().toString(), null);
			createCellWithString(row.createCell(columnIndex++), studentDto.getTrainingCenterId().toString(), null);






		
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private static void createCellWithDate(Cell cell,Date date, CellStyle cellStyle) {
		cell.setCellValue(date);
		if(cellStyle != null) {
			cell.setCellStyle(cellStyle);
		}
	}
	

	private static void createCellWithString(Cell cell,String value, CellStyle cellStyle) {
		cell.setCellValue(value.toString());
		if(cellStyle != null) {
			cell.setCellStyle(cellStyle);
		}
	}
	
	private static XSSFCellStyle getCellStyle(XSSFWorkbook workbook, String type , String fontWeight) {
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFDataFormat df = workbook.createDataFormat();

		// Type
		if(type != null) {
			if(type.equalsIgnoreCase("date")) {
				cellStyle.setDataFormat(df.getFormat("mm/dd/yyyy"));
			} else if(type.equalsIgnoreCase("currency")) {
				cellStyle.setDataFormat(df.getFormat("#,##0.00"));		
			}	
		}
		
		// font weight
		if(fontWeight != null && fontWeight.equalsIgnoreCase("bold")) {
			 XSSFFont font= workbook.createFont();
			 font.setBold(true);
			cellStyle.setFont(font);
		}
		return cellStyle;
	}
}
