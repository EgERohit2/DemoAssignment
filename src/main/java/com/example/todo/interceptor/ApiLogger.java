package com.example.todo.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.todo.entities.ApiLoggerEntity;
import com.example.todo.services.ApiLoggerSerivceInterface;

public class ApiLogger implements HandlerInterceptor {

	public ApiLogger() {

		// TODO Auto-generated constructor stub
	}

//	@Autowired
//	private LoggerService loggerServiceInterface;

	@Autowired
	private ApiLoggerSerivceInterface apiLoggerSerivceInterface;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String[] arr = request.getRequestURI().split("/");
		String getParam = arr[arr.length - 1];
		String getParam2 = arr[arr.length - 2];
		ArrayList<String> skipUrls = new ArrayList<>(Arrays.asList("/auth/generatetoken", "/auth/forgot-pass",
				"/file/downloadFile/" + getParam2 + "/" + getParam));

		if (!skipUrls.contains(request.getRequestURI())) {

			ApiLoggerEntity apiDetail = new ApiLoggerEntity();
//	apiDetail.setUserToken(requestTokenHeader);
			apiDetail.setIpAddress(request.getRemoteAddr());
			apiDetail.setUrl(request.getRequestURI());
			apiDetail.setMethod(request.getMethod());
			apiDetail.setHost(request.getRemoteHost());
			apiDetail.setBody(request instanceof StandardMultipartHttpServletRequest ? null
					: request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
			apiLoggerSerivceInterface.createApiLog(apiDetail);
			return true;

		} else

		{
			return true;

		}
	}
}