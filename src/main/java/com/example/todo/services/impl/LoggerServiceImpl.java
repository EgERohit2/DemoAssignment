package com.example.todo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.CacheOperation;
import org.springframework.stereotype.Service;

import com.example.todo.dto.LoggerDto;
import com.example.todo.entities.LoggerEntity;
import com.example.todo.entities.User;
import com.example.todo.repository.LoggerRepository;
import com.example.todo.services.LoggerService;

@Service("LoggerServiceImpl")
public class LoggerServiceImpl implements LoggerService {

	public LoggerServiceImpl() {
		// TODO Auto-generated constructor stub
	}
	// @Autowired
	// private EntityManagerFactory entityManagerFactory;
	//
	// @Autowired
	// private UserServiceInterface userServiceInterface;

	@Autowired
	private LoggerRepository loggerRepository;

//	@Autowired
//	private CacheOperation cache;



	@Override
	public LoggerEntity createLogger(LoggerDto loggerDto, User user) {
		LoggerEntity logger = new LoggerEntity();
		logger.setUserId(user);
		logger.setToken(loggerDto.getToken());
		logger.setExpireAt(loggerDto.getExpireAt());
		LoggerEntity loggerEntity = loggerRepository.save(logger);
		return loggerEntity;
	}



	@Override
	public LoggerEntity getLoggerDetail(String token) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void logoutUser(String token, Long userId, String email) {
		// TODO Auto-generated method stub
		
	}

	
}