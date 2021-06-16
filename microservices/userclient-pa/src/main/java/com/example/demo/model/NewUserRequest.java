package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
	
	private String userid;
	private String password;
	private String name;
	private int age;
	private String role;
	private String department;
	private String salary;
	
	public NewUserRequest(User user, PayrollDTO payroll) {
		this.userid = user.getUserid();
		this.password = user.getPassword();
		this.name = user.getName();
		this.age = user.getAge();
		this.role = user.getRole();
		this.department = payroll.getDepartment();
		this.salary = payroll.getSalary();
	}
	

}
