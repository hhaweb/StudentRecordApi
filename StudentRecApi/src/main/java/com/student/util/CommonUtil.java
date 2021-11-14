package com.student.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import com.google.gson.reflect.TypeToken;
import com.student.dto.Menus;
import com.student.dto.UserDetailsImpl;
import com.google.gson.Gson;

import java.util.Scanner;
@Component
public class CommonUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	@Autowired
	ResourceLoader resourceLoader;
	
	public List<Menus> getMenuItem() {
		Gson gson = new Gson();
		Resource resource = resourceLoader.getResource("classpath:top-menu-items.json");
	    InputStream inputStream;	 
	    try
	    {
	    	inputStream = resource.getInputStream();
	        byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
	        String jsonStr = new String(bdata, StandardCharsets.UTF_8);	        
	       List<Menus> menuList = gson.fromJson(jsonStr, new TypeToken<List<Menus>>(){}.getType());
	        
	        return menuList;	
	    } 
	    catch (IOException e) 
	    {
	    	e.printStackTrace();
	    }
		return null;
	}
	
	public String generatePassword() {
		String randomPasswords = "";

		for(int i = 0; i < 7; i++) {
			randomPasswords += randomCharacter();
		}
		return randomPasswords;
	}
	

	public UserDetailsImpl getCurrentLoginUser() {
		UserDetailsImpl userDetails = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userDetails;
	}
	
	public static String getPasswordStrength(int length) {
		if(length < 5) {
			return "weak";
		} else if(length < 10) {
			return "medium";
		} else {
			return "strong";
		}
	}

		private static char randomCharacter() {
			//We multiply Math.random() by 62 because there are 26 lowercase letters, 26 uppercase letters, and 10 numbers, and 26 + 26 + 10 = 62
			//This random number has values between 0 (inclusive) and 62 (exclusive)
			int rand = (int)(Math.random()*62);
			
			//0-61 inclusive = all possible values of rand
			//0-9 inclusive = 10 possible numbers/digits
			//10-35 inclusive = 26 possible uppercase letters
			//36-61 inclusive = 26 possible lowercase letters
			//If rand is between 0 (inclusive) and 9 (inclusive), we can say it's a number/digit
			//If rand is between 10 (inclusive) and 35 (inclusive), we can say it's an uppercase letter
			//If rand is between 36 (inclusive) and 61 (inclusive), we can say it's a lowercase letter
			if(rand <= 9) {
				//Number (48-57 in ASCII)
				//To convert from 0-9 to 48-57, we can add 48 to rand because 48-0 = 48
				int number = rand + 48;
				//This way, rand = 0 => number = 48 => '0'
				//Remember to cast our int value to a char!
				return (char)(number);
			} else if(rand <= 35) {
				//Uppercase letter (65-90 in ASCII)
				//To convert from 10-35 to 65-90, we can add 55 to rand because 65-10 = 55
				int uppercase = rand + 55;
				//This way, rand = 10 => uppercase = 65 => 'A'
				//Remember to cast our int value to a char!
				return (char)(uppercase);
			} else {
				//Lowercase letter (97-122 in ASCII)
				//To convert from 36-61 to 97-122, we can add 61 to rand because 97-36 = 61
				int lowercase = rand + 61;
				//This way, rand = 36 => lowercase = 97 => 'a'
				//Remember to cast our int value to a char!
				return (char)(lowercase);
			}
		}

}
