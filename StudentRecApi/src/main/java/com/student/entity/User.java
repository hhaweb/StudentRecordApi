package com.student.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(	name = "user", 
		uniqueConstraints = {
			@UniqueConstraint(columnNames = "user_name") 
		})
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank
	@Column(name = "user_name")
	private String userName;

	@Column(name = "email")
	private String email;
	
	@NotBlank
	@Size(max = 120)
	@Column(name = "password")
	private String password;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_date")
	private Date updateDate;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="role_id")
	private Role role;

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User( @NotBlank String userName, String email, @NotBlank @Size(max = 120) String password,
			Date createDate, Date updateDate, Role role) {
		super();
		this.userName = userName;
		if(email != null && !email.isEmpty()) {
			this.email = email;
		}
		this.password = password;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.role = role;
	}
}
