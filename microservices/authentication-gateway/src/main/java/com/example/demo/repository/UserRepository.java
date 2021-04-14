package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.AuthenticationUser;


@Repository
public interface UserRepository extends CrudRepository<AuthenticationUser, String>{

	AuthenticationUser findByUserid(String userid);

}
