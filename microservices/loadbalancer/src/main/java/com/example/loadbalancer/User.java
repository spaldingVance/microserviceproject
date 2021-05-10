

package com.example.loadbalancer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//
//public class User {
//	
//	public String userid;
//	public String password;
//	public String name;
//	public int age;
//	public String role;
//	private String department;
//	private String salary;
//	
//
//}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
	private String userid;
	private String password;
	private String name;
	private int age;
	private String role;
	private String department;
	private String salary;
	
}