package com.student.dto.csv;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.opencsv.bean.CsvBindByName;
import com.student.config.ConfigData;
import com.student.dto.StudentDto;
import com.student.entity.Student;
import com.student.util.CSVHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StudentCsvDto {
	@CsvBindByName(column = "id")
	private String id;

	@CsvBindByName(column = "name")
	private String name;

	@CsvBindByName(column = "cid")
	private String cid;

	@CsvBindByName(column = "did")
	private String did;

	@CsvBindByName(column = "date_of_birth")
	private String dateOfBirth;

	@CsvBindByName(column = "email")
	private String email;

	@CsvBindByName(column = "mobile_no")
	private String mobileNo;

	@CsvBindByName(column = "gender")
	private String gender;

	@CsvBindByName(column = "blood_group")
	private String bloodGroup;

	@CsvBindByName(column = "marital_status")
	private String martialStatus;

	@CsvBindByName(column = "employment_type_id")
	private String employmentTypeId;

	@CsvBindByName(column = "avatar")
	private String avatar;

	@CsvBindByName(column = "status")
	private String status;

	@CsvBindByName(column = "user_id")
	private String userId;

	@CsvBindByName(column = "training_center_id")
	private String trainingCenterId;

	@CsvBindByName(column = "created_at")
	private String createdAt;

	@CsvBindByName(column = "updated_at")
	private String updatedAt;

	@CsvBindByName(column = "deleted_at")
	private String deletedAt;

	@CsvBindByName(column = "batch_no")
	private String batchNo;

	@CsvBindByName(column = "training_year")
	private String trainingYear;

	@CsvBindByName(column = "Remark")
	private String errorMessage;

	private boolean isHaveError;

	public Student getEntity() throws ParseException {
		DateFormat timeForamt = new SimpleDateFormat(ConfigData.DateFormatWithTime);
		DateFormat dateFormat = new SimpleDateFormat(ConfigData.DateFormat);
		Student student = new Student();
		if (this.id != null && !this.id.isEmpty() && CSVHelper.isNumeric(this.id)) {
			student.setId(Long.parseLong(this.id));
		}
		if (CSVHelper.validateDateFormat(this.createdAt, ConfigData.DateFormatWithTime)) {
			student.setCreatedDate(timeForamt.parse(this.createdAt));
		}
		if (CSVHelper.validateDateFormat(this.updatedAt, ConfigData.DateFormatWithTime)) {
			student.setUpdatedDate(timeForamt.parse(this.updatedAt));
		}
		if (CSVHelper.validateDateFormat(this.deletedAt, ConfigData.DateFormatWithTime)) {
			student.setDeletedDate(timeForamt.parse(this.deletedAt));
		}

		if (CSVHelper.validateDateFormat(this.dateOfBirth, ConfigData.DateFormat)) {
			student.setDateOfBirth(dateFormat.parse(this.dateOfBirth));
		}

		if (CSVHelper.isNumeric(this.employmentTypeId)) {
			student.setEmploymentTypeId(Long.parseLong(this.employmentTypeId));
		}

		if (CSVHelper.isNumeric(this.userId)) { // user from student csv upload
			student.setUserId(Long.parseLong(this.userId));
		}
		if (CSVHelper.isNumeric(this.trainingCenterId)) {
			student.setTrainingCenterId(Long.parseLong(this.trainingCenterId));
		}
		if (CSVHelper.isNumeric(this.batchNo)) {
			student.setBatchNo(Integer.parseInt(this.batchNo));
		}
		if (CSVHelper.isNumeric(this.trainingYear)) {
			student.setTrainingYear(Integer.parseInt(this.trainingYear));
		}
		student.setInDate(new Date());
		student.setCid(this.cid);
		student.setDid(this.did);
		student.setName(this.name);
		student.setMobileNo(this.mobileNo);
		student.setGender(this.gender);
		student.setBloodGroup(this.bloodGroup);
		student.setMaritalStatus(this.martialStatus);
		student.setAvatar(this.avatar);
		student.setStatus(this.status);
		return student;

	}

	public StudentCsvDto(StudentDto studentDto) {
		super();
		if (studentDto.getId() != null) {
			this.id = studentDto.getId().toString();

		}
		this.name = studentDto.getName();
		this.cid = studentDto.getCid();
		this.did = studentDto.getDid();
		this.dateOfBirth = studentDto.getDateOfBirth();
		this.email = studentDto.getEmail();
		this.mobileNo = studentDto.getMobileNo();
		this.gender = studentDto.getGender();
		this.bloodGroup = studentDto.getBloodGroup();
		this.martialStatus = studentDto.getMaritalStatus();
		this.status = studentDto.getStatus();
		this.userId = studentDto.getUserId() != null ? studentDto.getUserId().toString() : null;
		this.trainingCenterId = studentDto.getTrainingCenterId() != null ? studentDto.getTrainingCenterId().toString()
				: null;
		this.createdAt = studentDto.getCreatedDate();
		this.deletedAt = studentDto.getDeletedDate();
		this.updatedAt = studentDto.getUpdatedDate();
		this.batchNo = studentDto.getBatchNo() != null ? studentDto.getBatchNo().toString() : null;
		this.trainingYear = studentDto.getTrainingYear() != null ? studentDto.getTrainingYear().toString() : null;

	}
}
