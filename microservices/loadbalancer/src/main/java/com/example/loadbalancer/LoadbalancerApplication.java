package com.example.loadbalancer;

import reactor.core.publisher.Mono;

import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;



//import com.example.demo.model.AuthenticationRequest;

import org.springframework.boot.autoconfigure.SpringBootApplication;

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

//	@RequestMapping("/hi")
//	public Mono<String> hi(@RequestParam(value = "name", defaultValue = "Mary") String name) {
//		return loadBalancedWebClientBuilder.build().get().uri("http://say-hello/greeting").retrieve()
//				.bodyToMono(String.class).map(greeting -> String.format("%s, %s!", greeting, name));
//	}
//
//	@RequestMapping("/authenticate")
//	public Mono<String> createAuthenticationToken(String authenticationRequest) {
//		logger.info("inside authenticate");
//		logger.info(authenticationRequest);
//		return loadBalancedWebClientBuilder.build().post().uri("http://authentication-gateway/authenticate")
//				.retrieve()
//				.bodyToMono(String.class);
////		return loadBalancedWebClientBuilder.build().post().uri("http://authentication-gateway/authenticate")
////				.accept(MediaType.ALL)
////				.exchangeToMono(clientResponse -> clientResponse.bodyToMono(ResponseEntity.class));
////				
//
//	}
	
	@PostMapping("/authenticate")
	public Mono<String> createAuthenticationToken(
			@RequestBody String authenticationRequest) throws Exception {
		return loadBalancedWebClientBuilder.build().post().uri("http://authentication-gateway/authenticate")
				.body(Mono.just(authenticationRequest), String.class)
				.retrieve()
				.bodyToMono(String.class);
	}

}
