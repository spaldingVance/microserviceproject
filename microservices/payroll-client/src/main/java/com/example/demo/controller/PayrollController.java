package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.NewPayrollRequest;
import com.example.demo.model.Payroll;
import com.example.demo.service.PayrollService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("payroll")
public class PayrollController {

	@Autowired
	private PayrollService payrollService;

	@GetMapping("/{userid}")
	public ResponseEntity<?> getPayroll(@PathVariable @Valid String userid) {
		Payroll payroll = payrollService.findById(userid);
		
		System.out.println("at /payroll/{userid}");

		
		return new ResponseEntity<Payroll>(payroll, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<Payroll> registerUser(@RequestBody NewPayrollRequest payrollRequest) throws JsonMappingException, JsonProcessingException {
	
		Payroll registeredPayroll = payrollService.saveNewPayroll(payrollRequest);
		
		if(registeredPayroll != null) {
			return new ResponseEntity<Payroll>(registeredPayroll, HttpStatus.OK);
		} else {
			return new ResponseEntity<Payroll>(registeredPayroll, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/update")
	public ResponseEntity<Payroll> updatePayroll(@Valid @RequestBody NewPayrollRequest payrollRequest) throws JsonMappingException, JsonProcessingException {

		Payroll payroll = payrollService.findById(payrollRequest.getUserid());
		payroll.setDepartment(payrollRequest.getDepartment());
		payroll.setSalary(payrollRequest.getSalary());

		Payroll updatedPayroll = payrollService.updatePayroll(payroll);
		if(updatedPayroll != null) {
			return new ResponseEntity<Payroll>(updatedPayroll, HttpStatus.OK);
		} else {
			return new ResponseEntity<Payroll>(updatedPayroll, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}