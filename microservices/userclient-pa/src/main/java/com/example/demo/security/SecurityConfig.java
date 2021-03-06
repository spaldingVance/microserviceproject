//package com.example.demo.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.BeanIds;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import com.example.demo.service.UserDetailsServiceImpl;
//import com.example.demo.service.UserService;
//
////@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Autowired
//	UserDetailsServiceImpl myUserDetailsService;
//
////	@Autowired
////	private JwtRequestFilter jwtRequestFilter;
//
//	@Autowired
//	private JwtAuthenticationEntryPoint unauthorizedHandler;
//
//
//	@Bean
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
//
//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//		return source;
//	}
//
//	@Bean
//	public BCryptPasswordEncoder createBCryptBean() {
//		return new BCryptPasswordEncoder();
//	}
//
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//		auth.userDetailsService(myUserDetailsService).passwordEncoder(createBCryptBean());
//	} 
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//
//		http.cors().and().csrf().disable().authorizeRequests()
//				.antMatchers("/user/login").permitAll()
//				.antMatchers("/user/register").permitAll()
//				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//				.antMatchers(HttpMethod.POST, "/authenticate/**").permitAll()
//				.antMatchers("/user/**").hasAnyRole("USER")
//				.antMatchers(HttpMethod.POST, "/user/update").permitAll()//hasAnyRole("USER")
//				.and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and().sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//			
//
//		http.cors();
//
////		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//	}
//
//}
