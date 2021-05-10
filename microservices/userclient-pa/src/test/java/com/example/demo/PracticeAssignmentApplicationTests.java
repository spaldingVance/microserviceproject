package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.demo.model.User;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;





import com.example.demo.model.NewUserRequest;
import com.example.demo.service.UserService;

@SpringBootTest
class PracticeAssignmentApplicationTests {
	
	@Autowired
	private UserService userService;

	@Test
	void contextLoads() {
	}
	
	
	
	@Test
	@DisplayName("Should save new user")
	public void shouldSaveNewUser() {
		NewUserRequest newUserRequest = new NewUserRequest("9865", "pass", "Mike", 40, "Engineer", "Software", "50000");
		NewUserRequest savedUser = userService.saveNewUser(newUserRequest);
		assertFalse(savedUser == null);
	}
	
//	@Test
//	@DisplayName("Should not save a new user")
//	public void shouldNotSaveNewUser() {
//		NewUserRequest newUserRequest = new NewUserRequest("9865", "pass", "Mike", 40, "Engineer", "Software", "50000");
//		Exception exception = assertThrows<Exception> ("Should throw an exception if input is invalid" {
//			NewUserRequest savedUser = userService.saveNewUser(newUserRequest);
//		}
//
//	}
	
	@Test
	@DisplayName("Should Find by ID")
	public void shouldFindById() {
		NewUserRequest newUserRequest = new NewUserRequest("9865", "pass", "Mike", 40, "Engineer", "Software", "50000");
		NewUserRequest savedUser = userService.saveNewUser(newUserRequest);
//		User user = userService.findById("9865");
		assertEquals(savedUser.getName(), "Mike");
	}
	

}
