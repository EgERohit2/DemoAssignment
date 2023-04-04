package com.example.todo.exceptionHandling;

import java.io.IOException;
import java.net.InetAddress;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.todo.dto.ErrorResponseDto;
import com.example.todo.entities.ErrorLoggerEntity;
import com.example.todo.entities.MethodEnum;
import com.example.todo.repository.ErrorloggerRepository;

@ControllerAdvice
public class AllExceptionHandler extends ResponseEntityExceptionHandler{

	@Autowired
	private ErrorloggerRepository errorloggerRepository;
	

	@ExceptionHandler(DataNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	void dataResourceNotFound(DataNotFoundException f) {
		f.getMessage();
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ErrorResponseDto handleException(final Exception exception, HttpServletRequest httpServletRequest) throws IOException {
		
		ErrorLoggerEntity errorRequest = new ErrorLoggerEntity();
		errorRequest.setBody(httpServletRequest instanceof StandardMultipartHttpServletRequest ? null : httpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
		errorRequest.setHost(InetAddress.getLoopbackAddress().getHostAddress());
		errorRequest.setMessage(exception.getMessage());
		errorRequest.setMethod(Enum.valueOf(MethodEnum.class, httpServletRequest.getMethod()));
		errorRequest.setToken(httpServletRequest.getHeader(PAGE_NOT_FOUND_LOG_CATEGORY));
		errorRequest.setUrl(httpServletRequest.getRequestURI());
		errorloggerRepository.save(errorRequest);
		ErrorResponseDto error = new ErrorResponseDto();
		error.setMessage("error");
		error.setMsgKey("found");
		return error;
		
	}
	
	
}
