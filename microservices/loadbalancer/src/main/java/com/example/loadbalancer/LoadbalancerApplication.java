package com.example.loadbalancer;

import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//import com.example.demo.model.AuthenticationRequest;


@SpringBootApplication
@EnableDiscoveryClient
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class LoadbalancerApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(LoadbalancerApplication.class.getName());
	

	private final WebClient.Builder loadBalancedWebClientBuilder;
	private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

	public LoadbalancerApplication(WebClient.Builder webClientBuilder,
			ReactorLoadBalancerExchangeFilterFunction lbFunction) {
		this.loadBalancedWebClientBuilder = webClientBuilder;
		this.lbFunction = lbFunction;
	}

	public static void main(String[] args) {
		SpringApplication.run(LoadbalancerApplication.class, args);
	}

	
	@PostMapping("/authenticate")
	public Mono<String> createAuthenticationToken(
			@RequestBody String authenticationRequest) throws Exception {
		return loadBalancedWebClientBuilder.build().post().uri("http://authentication-gateway/authenticate")
				.body(Mono.just(authenticationRequest), String.class)
				.retrieve()
				.bodyToMono(String.class);
	}
	
	@GetMapping("/user/{userId}")
	public Mono<String> getUser(@PathVariable String userId ) {
		
		return loadBalancedWebClientBuilder.build().get().uri("http://authentication-gateway/user/" + userId)

				.retrieve()
				.bodyToMono(String.class);
	}
	
//	@PostMapping("/authenticate/new")
	
//	@PostMapping("/user/update")
	
//	@RequestMapping("/user/welcome")

	@PostMapping("/verify")
	public Mono<String> getVerified(@RequestBody String request) throws JsonMappingException, JsonProcessingException {
		logger.info("verify in loadbalancer");
		HashMap<String,HashMap<String, String>> req;
		logger.info("req created");
		
		req = new ObjectMapper().readValue(request, new TypeReference<HashMap<String, HashMap<String, String>>>() {});
		
		logger.info("deserialized object");
		logger.info(req.get("headers").get("Authorization"));

		logger.info("successfully deserialized request");		
		return loadBalancedWebClientBuilder.build().post().uri("http://authentication-gateway/verify")
				.body(Mono.just(req), HashMap.class)
				.header("Authorization", req.get("headers").get("Authorization"))
				.retrieve()
				.bodyToMono(String.class);
	}

}
