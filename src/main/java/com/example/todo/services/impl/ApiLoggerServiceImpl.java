package com.example.todo.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.entities.ApiLoggerEntity;
import com.example.todo.repository.ApiLoggerRepository;
import com.example.todo.services.ApiLoggerSerivceInterface;

@Service("apiLoggerServiceImpl")
public class ApiLoggerServiceImpl implements ApiLoggerSerivceInterface {

	private static final Logger LOG = LoggerFactory.getLogger(ApiLoggerServiceImpl.class);

	public ApiLoggerServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private ApiLoggerRepository apiLoggerRepository;

	@Override
	public void createApiLog(ApiLoggerEntity api) {
		LOG.info(" >> createApiLog(ApiLoggerEntity api) >> Start");
		apiLoggerRepository.save(api);
	}
}