package com.example.todo.services;

import java.util.List;
import java.util.Optional;

import com.example.todo.entities.UserTask;

public interface UserTaskService {

	public UserTask saveUserTask(UserTask userTask);

	public List<UserTask> getAllUserTasks();

	public Optional<UserTask> getUserTaskById(int id);

	public UserTask updateUserTask(int id, UserTask userTask);

}
