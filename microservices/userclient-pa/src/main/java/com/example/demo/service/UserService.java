package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.NewUserRequest;
import com.example.demo.model.PayrollDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;

	public NewUserRequest saveNewUser(@Valid NewUserRequest userRequest) {
		System.out.println(userRequest.getUserid());
		User user = new User(userRequest.getUserid(), userRequest.getPassword(),userRequest.getName(), userRequest.getAge(), userRequest.getRole());
		User userSaved = userRepository.save(user);
		PayrollDTO payroll = new PayrollDTO(userRequest.getUserid(), userRequest.getDepartment(), userRequest.getSalary());
		PayrollDTO payrollSaved = setPayroll(payroll);
		if(userSaved != null && payrollSaved != null) {
			return userRequest;
		} else {
			System.out.println("error saving user");
			return null;
		}

	}

	public NewUserRequest findById(String userid) {
		User user = userRepository.findByUserid(userid);
		PayrollDTO payroll = getPayroll(userid);
		
		System.out.println("Found User " + user.getUserid());
		System.out.println("payroll: " + payroll.getDepartment());
		System.out.println(user.getAge());
		//return user;
		return new NewUserRequest(user, payroll);
	}

	public NewUserRequest updateUser(@Valid NewUserRequest user) {
		
		User userInDb = userRepository.findByUserid(user.getUserid());
		System.out.println(user.getName());
		userInDb.setName(user.getName());
		userInDb.setAge(user.getAge());
		userInDb.setPassword(user.getPassword());
		userInDb.setUserid(user.getUserid());
		userInDb.setRole(user.getRole());
		System.out.println(userInDb.getName());
		System.out.println(userInDb.getAge());
		System.out.println(userInDb.getPassword());
		System.out.println(userInDb.getUserid());
		User updatedUser = userRepository.save(userInDb);
		
		
		PayrollDTO payroll = new PayrollDTO(user.getUserid(), user.getDepartment(), user.getSalary());
		PayrollDTO updatedPayroll = updatePayroll(payroll);
		if(updatedUser != null && updatedPayroll != null) {
			return user;
		} else {
			return null;
		}
		
	}
	
	public PayrollDTO getPayroll(String userid) {
		String uri = "http://localhost:8085/payroll/{userid}";
		Map<String, String> map = new HashMap<>();
		map.put("userid", userid);
		PayrollDTO payroll = new RestTemplate().getForObject(uri, PayrollDTO.class, map);
		System.out.println(payroll);
		return payroll;
	}
	
	public PayrollDTO updatePayroll(PayrollDTO payroll) {
		String uri = "http://localhost:8085/payroll/update";
//		PayrollDTO payroll = new RestTemplate().getForObject(uri, PayrollDTO.class, map);
		PayrollDTO payrollUpdated = new RestTemplate().postForObject(uri, payroll, PayrollDTO.class);
		
		return payrollUpdated;

	}
	
	public PayrollDTO setPayroll(PayrollDTO payroll) {
		String uri = "http://localhost:8085/payroll/register";
//		PayrollDTO payroll = new RestTemplate().getForObject(uri, PayrollDTO.class, map);
		
		PayrollDTO payrollUpdated = new RestTemplate().postForObject(uri, payroll, PayrollDTO.class);
		
//		URI
		return payrollUpdated;

	}
	
}

