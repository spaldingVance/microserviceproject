package com.example.demo.security;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.service.UserDetailsServiceImpl;
import com.example.demo.service.UserService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	UserDetailsServiceImpl myUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private AccessDeniedHandlerJwt accessDeniedHandlerJwt;


	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//		return source;
//	}

	@Bean
	public PasswordEncoder createBCryptBean() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(myUserDetailsService).passwordEncoder(createBCryptBean());
	} 
	
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

//        if (Boolean.parseBoolean(env.getRequiredProperty("security.disable.csrf")))
//            httpSecurity.csrf().disable();
//		.cors().disable()
//      .httpBasic().disable()
//      .formLogin().disable()
        httpSecurity
        		.cors().and().csrf().disable()
        		.authorizeRequests()
                	.antMatchers("/authenticate").permitAll()
                	.antMatchers("/authenticate/new").permitAll()
                	.antMatchers("/verify").permitAll()
                	.antMatchers("/user/login").permitAll()
                	.antMatchers("/user/register").permitAll()
                	.antMatchers("/user/**").hasRole("USER")
                .anyRequest().permitAll().and()
                	.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint);//.and()
//                .exceptionHandling().accessDeniedHandler(accessDeniedHandlerJwt).and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
//        httpSecurity.cors(); // just added
//        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//
//		http.cors().and().csrf().disable().authorizeRequests().antMatchers("/user/login").permitAll()
//				.antMatchers("/user/register").permitAll()
//				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//				.antMatchers(HttpMethod.POST, "/authenticate/**").permitAll()
//				.antMatchers(HttpMethod.POST, "/verify/**").permitAll()
//				.antMatchers("/user/**").hasRole("USER")
//				.anyRequest().authenticated()
//				.and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and().sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//			
//
//		http.cors();
//
//		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//	}

}
