package com.example.todo.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.todo.interceptor.AssignmentHandler;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer{

	@Autowired
	private AssignmentHandler assignmentHandler;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(assignmentHandler);
	}
}
