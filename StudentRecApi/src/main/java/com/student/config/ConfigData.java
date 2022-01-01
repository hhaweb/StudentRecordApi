package com.student.config;

import java.util.Arrays;
import java.util.List;

public class ConfigData {
	public static final List<String> StudentCSVHeader = Arrays.asList("id", "name", "cid", "did", "date_of_birth",
			"email", "mobile_no", "gender", "blood_group", "marital_status", "employment_type_id", "avatar", "status",
			"user_id", "training_center_id", "created_at", "updated_at", "deleted_at", "batch_no", "training_year");

	public static final List<String> StudentCSVHeaderError = Arrays.asList("id", "name", "cid", "did", "date_of_birth",
			"email", "mobile_no", "gender", "blood_group", "marital_status", "employment_type_id", "avatar", "status",
			"user_id", "training_center_id", "created_at", "updated_at", "deleted_at", "batch_no", "training_year",
			"Remark");

	public static final List<String> CourseCSVHeader = Arrays.asList("Course ID", "Course Name", "Course Status", "Industrial Sector",
			"Course Level", "Duration", "Start Date", "End Date", "Cohort Size Male", "Cohort Size Female",
			"Number of Applicants Male", "Number of Applicants Female", "No. of Students Certified Male","No. of Students Certified Female","Batch Number","Training Location",
			"Trainer ID","Student Name","CID","DID");
	
	public static final List<String> CourseCSVHeaderError = Arrays.asList("Course ID", "Course Name", "Course Status", "Industrial Sector",
			"Course Level", "Duration", "Start Date", "End Date", "Cohort Size Male", "Cohort Size Female",
			"Number of Applicants Male", "Number of Applicants Female", "No. of Students Certified Male","No. of Students Certified Female","Batch Number","Training Location",
			"Trainer ID","Student Name","CID","DID","Remark");

	public static final List<String> TrainerCSVHeader = Arrays.asList("Trainer ID", "Trainer Name", "Sex", "Country","Date of Joining","Designation","Department",
			"Branch/ Area of Expertise","DSP Centre","Training Programme","Trainer Affiliation","Qualification");
	
	public static final List<String> TrainerCSVHeaderError = Arrays.asList("Trainer ID", "Trainer Name", "Sex", "Country","Date of Joining","Designation","Department",
			"Branch/ Area of Expertise","DSP Centre","Training Programme","Trainer Affiliation","Qualification","Remark");

	public static final String errorFileName = "_error.csv";

	public static final String DateFormatWithTime = "d/MM/yyyy H:mm";
	public static final String DateFormat = "d/MM/yyyy";

	public static final List<String> Super_Admin = Arrays.asList("Students", "Trainer", "Course", "Upload");
	public static final List<String> Admin = Arrays.asList("Students", "Trainer", "Course");

	public static final List<String> Super_Admin_Route = Arrays.asList("student/student-list",
			"student/student-details", "upload/data-upload", "upload/data-upload-history", "course/course-info","course/course-list",
			"trainer/trainer-list", "trainer/trainer-detail", "user/user-list");

	public static final List<String> Admin_Route = Arrays.asList("student/student-list", "student/student-details",
			"course/course-info","course/course-list", "trainer/trainer-list", "trainer/trainer-detail", "user/user-list");

}
