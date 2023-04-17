package com.example.todo.component;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	/**
	 * 
	 */

	private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationEntryPoint.class);
	private static final long serialVersionUID = -7858869558953243875L;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		LOG.info("CONFIGURE >> JwtAuthenticationEntryPoint >> commence() >>");
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getOutputStream().println("{\r\n" + "	\"message\": \"Unauthorized\",\r\n" + "	\"msgKey\": \"unauthorized\"\r\n" + "}");
		LOG.info("CONFIGURE >> JwtAuthenticationEntryPoint >> commence() >>");
	}


}
