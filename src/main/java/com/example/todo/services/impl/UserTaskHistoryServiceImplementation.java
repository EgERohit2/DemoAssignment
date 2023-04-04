package com.example.todo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.entities.UserTaskHistory;
import com.example.todo.repository.UserTaskHistoryRepository;
import com.example.todo.services.UserTaskHistoryService;

@Service
public class UserTaskHistoryServiceImplementation implements UserTaskHistoryService {

	@Autowired
	private UserTaskHistoryRepository userTaskHistoryRepository;

	@Override
	public UserTaskHistory saveUserTaskHistory(UserTaskHistory userTaskHistory) {
		// TODO Auto-generated method stub
		return userTaskHistoryRepository.save(userTaskHistory);
	}

}
