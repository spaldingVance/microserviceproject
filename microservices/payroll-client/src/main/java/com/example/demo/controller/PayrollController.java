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
		
		return new ResponseEntity<Payroll>(payroll, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody String payrollRequest) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Payroll payrollReq = mapper.readValue(payrollRequest, Payroll.class);
		
		System.out.println("at /user/register");
//		System.out.println(user.getUserid());
		
		Payroll registeredPayroll = payrollService.saveNewPayroll(payrollReq);
		
		if(registeredPayroll != null) {
			return new ResponseEntity<String>("Successfully registered in payroll service", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/update")
	public ResponseEntity<String> updatePayroll(@Valid @RequestBody String payrollRequest) throws JsonMappingException, JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		
		NewPayrollRequest payrollReq = mapper.readValue(payrollRequest, NewPayrollRequest.class);
		Payroll payroll = payrollService.findById(payrollReq.getUserid());

		Payroll updatedPayroll = payrollService.updatePayroll(payrollReq);
		if(updatedPayroll != null) {
			return new ResponseEntity<String>("Payroll Updated", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}