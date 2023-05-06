package com.example.todo.exceptionHandling;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.example.todo.dto.ErrorResponseDto;
import com.example.todo.entities.ErrorLoggerEntity;
import com.example.todo.entities.MethodEnum;
import com.example.todo.repository.ErrorloggerRepository;

//@ControllerAdvice
@RestControllerAdvice
public class AllExceptionHandler {

	@Autowired
	private ErrorloggerRepository errorLoggerRepository;

	@ExceptionHandler(DataNotFoundException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public @ResponseBody ExceptionResponse handleResourceNotFound(final ResourceNotFoundException exception,
			final HttpServletRequest request) {

		ExceptionResponse error = new ExceptionResponse();
		error.setErrorMessage(exception.getMessage());
		error.callerURL(request.getRequestURI());
		return error;

	}

	// (for reference- cv maker)
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
//	public @ResponseBody ErrorResponseDto handleValidatioinException(final MethodArgumentNotValidException exception) {
//
//		List<String> details = new ArrayList<>();
//
//		for (ObjectError error : exception.getBindingResult().getAllErrors()) {
//
//			details.add(error.getDefaultMessage());
//
//		}
//
//		ErrorResponseDto error = new ErrorResponseDto();
//		error.setMessage(details.get(0).split("\\*", 2)[0]);
//		error.setMsgKey(details.get(0).split("\\*", 2)[1]);
//		return error;
//
//	}

	// (checking) 18-04-2023
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();
		System.out.println("hello1");
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			System.out.println("hello2");
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
	public @ResponseBody ErrorResponseDto handleMethodNotSupportException(
			final HttpRequestMethodNotSupportedException exception) {

		ErrorResponseDto error = new ErrorResponseDto();
		error.setMessage("Invalid request, Please check URL");
		error.setMsgKey("invalidRequest");
		return error;

	}	

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public @ResponseBody ErrorResponseDto handleAccessDeniedException(final AccessDeniedException exception) {

		ErrorResponseDto error = new ErrorResponseDto();
		error.setMessage("Access Denied");
		error.setMsgKey("accessDenied");
		return error;

	}

	@ExceptionHandler(MultipartException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorResponseDto fileUploadException(final MultipartException exception) {

		ErrorResponseDto error = new ErrorResponseDto();
		error.setMessage("Please upload file");
		error.setMsgKey("pleaseUploadFile");
		return error;

	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorResponseDto noHandlerFoundException(final NoHandlerFoundException exception) {

		ErrorResponseDto error = new ErrorResponseDto();
		error.setMessage("URL not Found");
		error.setMsgKey("urlNotFound");
		return error;

	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ErrorResponseDto handleException(final Exception exception, HttpServletRequest request)
			throws IOException {

		ErrorLoggerEntity errorRequest = new ErrorLoggerEntity();
		errorRequest.setBody(request instanceof StandardMultipartHttpServletRequest ? null
				: request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
		errorRequest.setHost(InetAddress.getLoopbackAddress().getHostAddress());
		errorRequest.setMessage(exception.getMessage());
		errorRequest.setMethod(Enum.valueOf(MethodEnum.class, request.getMethod()));
		errorRequest.setToken(request.getHeader("Authorization"));
		errorRequest.setUrl(request.getRequestURI());
		errorLoggerRepository.save(errorRequest);
		ErrorResponseDto error = new ErrorResponseDto();
		error.setMessage("Something Went Wrong");
		error.setMsgKey("somethingWentWrong");
		return error;

	}

}
