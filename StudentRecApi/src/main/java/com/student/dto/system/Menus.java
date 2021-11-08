package com.student.dto.system;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class Menus implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String label;
    public String icon;
    public List<String> routerLink;
    public List<MenuItem> items;
}
