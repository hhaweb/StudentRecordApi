package com.student.dto.system;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class MenuItem {
	public String label;
    public String icon;
    public List<String> routerLink;
}
