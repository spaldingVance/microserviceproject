package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Payroll;


@Repository
public interface PayrollRepository extends CrudRepository<Payroll, String>{

	Payroll findByUserid(String userid);

}
