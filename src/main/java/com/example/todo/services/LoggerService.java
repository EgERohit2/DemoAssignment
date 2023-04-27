package com.example.todo.services;

import com.example.todo.dto.LoggerDto;
import com.example.todo.entities.LoggerEntity;
import com.example.todo.entities.User;

public interface LoggerService {

	LoggerEntity getLoggerDetail(String token);

	LoggerEntity createLogger(LoggerDto loggerDto, User user);

	void logoutUser(String token, Long userId, String email);
}
