package com.student.config;

import java.util.Arrays;
import java.util.List;

public class CSVConfig {
 public static final List<String> StudentCSVHeader = Arrays.asList("name", "cid", "did", "mobile_no", "gender", "perm_address", "Alternative No.", "Training");
 public static final List<String> StudentErrorCSVHeader = Arrays.asList("name", "cid", "did", "mobile_no", "gender", "perm_address", "Alternative No.", "Training","Remark");
 public static final String errorFileName = "_error.csv";
}
