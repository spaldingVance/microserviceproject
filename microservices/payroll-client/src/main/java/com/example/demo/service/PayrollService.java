package com.example.demo.service;

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

	public Payroll saveNewPayroll(Payroll newUser) {
		System.out.println("got to here");
		System.out.println(newUser.getUserid());
		Payroll user = payrollRepository.save(newUser);
		if(user != null) {
			System.out.println("successfully saved user in userclient");
		} else {
			System.out.println("error saving user");
		}
		
		return user;

	}

	public Payroll findById(String userid) {
		Payroll payroll = payrollRepository.findByUserid(userid);
		return payroll;
	}

	public Payroll updatePayroll(@Valid NewPayrollRequest payroll) {
		
		Payroll payrollInDb = payrollRepository.findByUserid(payroll.getUserid());

		payrollInDb.setDepartment(payroll.getDepartment());
		payrollInDb.setSalary(payroll.getSalary());

		Payroll updatedPayroll = payrollRepository.save(payrollInDb);
		return updatedPayroll;
	}
}

