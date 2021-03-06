package com.example.demo;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.AuthenticationUser;
import com.example.demo.model.NewUserRequest;
import com.example.demo.security.BcryptGenerator;
import com.example.demo.security.JwtRequestFilter;
import com.example.demo.service.UserService;

@SpringBootApplication(exclude=SecurityAutoConfiguration.class)
@EnableDiscoveryClient
public class PracticeAssignmentApplication {
	
	@Autowired
	private BcryptGenerator bcryptgenerator;

	public static void main(String[] args) {
		SpringApplication.run(PracticeAssignmentApplication.class, args);
	}
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(PracticeAssignmentApplication.class);
	
	@RestController
	@CrossOrigin(origins = "http://localhost:3000")
	public class GatewaySampleApplication {

		@Autowired
		private UserService userService;
		
		@GetMapping("/user/{userId}")
		public ResponseEntity<?> getUser(ProxyExchange<byte[]> proxy, @PathVariable String userId) throws Exception {
			return proxy.uri("http://localhost:8083/user/" + userId).get();
		}

		@PostMapping("/authenticate/new")
		public ResponseEntity<?> createNewUser(ProxyExchange<byte[]> proxy, @RequestBody NewUserRequest newUserRequest) {
			logger.info("Authenticate New");

			String passEncoded = bcryptgenerator.passwordEncoder(newUserRequest.getPassword());
			AuthenticationUser newUser = new AuthenticationUser(newUserRequest.getUserid(), passEncoded, "ROLE_USER");
//			AuthenticationUser newUser = new AuthenticationUser(newUserRequest.getUserid(), newUserRequest.getPassword(), "ROLE_USER");
			AuthenticationUser savedUser = userService.saveNewUser(newUser);
			
			if(savedUser != null) {
				logger.info("successfull saved user");
				logger.info("Redirecting");
				
			//	return proxy.uri("http://localhost:8083/user/register").post();
				return proxy.uri("http://localhost:8083/user/register").post(response -> ResponseEntity.status(response.getStatusCode()) //
	 					.headers(response.getHeaders()) //
	 					.body(response.getBody()) //
	 			);

			} else {
				logger.info("Error saving user to db");
				return new ResponseEntity<String>("Error saving auth user to db", HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
		
		@PostMapping("/user/update")
		public ResponseEntity<?> updateUser(ProxyExchange<byte[]> proxy) throws Exception {
			return proxy.uri("http://localhost:8083/user/update").post();
		}

		@RequestMapping("/user/welcome")
		public ResponseEntity<?> welcome(ProxyExchange<byte[]> proxy) throws Exception {
			return proxy.uri("http://localhost:8083/user/welcome").get();
		}
		
		
		

	}

}
