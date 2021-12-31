package com.student.dto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
	private Long roleId;
	private Long totalRecord;
	private String roleName;
	
	@JsonIgnore
	public User getEntity() throws ParseException {
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
	
	public UserDto(User user) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		this.id = user.getId();
		this.userName = user.getUserName();
		this.createDate = user.getCreateDate() != null ? df.format(user.getCreateDate()) : null;
		this.updateDate = user.getUpdateDate() != null ? df.format(user.getUpdateDate()) : null;
		this.roleName = user.getRole().getName().toString();
		this.roleId = user.getRole().getId();
	}

	
	public UserDto(UserDetailsImpl userDetail) {
		this.id = userDetail.getId();
		this.userName = userDetail.getUsername();
		this.roleName = userDetail.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList()).get(0);
	}
	
	
	
	
	
}
