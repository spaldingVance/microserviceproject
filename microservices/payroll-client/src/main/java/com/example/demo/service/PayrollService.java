package com.example.demo.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.NewPayrollRequest;
import com.example.demo.model.Payroll;
import com.example.demo.repository.PayrollRepository;

@Service
public class PayrollService {
	@Autowired
	PayrollRepository payrollRepository;

	public Payroll saveNewPayroll(NewPayrollRequest payrollRequest) {
		System.out.println("got to here");
		System.out.println(payrollRequest.getDepartment());
		Payroll payroll = new Payroll(payrollRequest.getUserid(), payrollRequest.getDepartment(), payrollRequest.getSalary());
		Payroll user = payrollRepository.save(payroll);
		if(user != null) {
			System.out.println("successfully saved user in userclient");
		} else {
			System.out.println("error saving user");
		}
		
		return user;

	}

	public Payroll findById(String userid) {
		Payroll payroll = payrollRepository.findByUserid(userid);
		Optional<Payroll> payrollDup = payrollRepository.findById(userid);
	
		System.out.println("payroll service findbyid - ");
		Boolean payrollExists = payrollRepository.existsById(userid);
		System.out.println("payroll exists = " + payrollExists.toString());
		System.out.println(payrollDup.get().getUserid());
		System.out.println(payroll.getUserid());
		return payroll;
	}

	public Payroll updatePayroll(@Valid Payroll payroll) {
		
		Payroll payrollInDb = payrollRepository.findByUserid(payroll.getUserid());

		payrollInDb.setDepartment(payroll.getDepartment());
		payrollInDb.setSalary(payroll.getSalary());

		Payroll updatedPayroll = payrollRepository.save(payrollInDb);
		return updatedPayroll;
	}
}

