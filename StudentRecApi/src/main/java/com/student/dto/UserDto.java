package com.student.dto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.student.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
	private Long id;
	private String userName;
	private String createDate;
	private String updateDate;
	private String password;
	private List<String> role;
	
	public User getUser() throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		User user = new User();
		if(this.id != null) { // edit user
			user.setId(this.id);
			user.setCreateDate(df.parse(this.createDate));
			user.setUpdateDate(new Date());
		} else {	// create new user
			user.setCreateDate(new Date());
			user.setUpdateDate(new Date());
			user.setPassword(this.password);
		}
		
		user.setUserName(this.userName);
		return user;
		
	}
}
