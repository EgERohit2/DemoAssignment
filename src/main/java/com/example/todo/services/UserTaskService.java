package com.example.todo.services;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.todo.dto.UserTaskDto;
import com.example.todo.entities.TaskStatus;
import com.example.todo.entities.UserTask;

public interface UserTaskService {

	public UserTask saveUserTask(UserTask userTask);

	public List<UserTask> getAllUserTasks();

	public Optional<UserTask> getUserTaskById(int id);

	public UserTask updateUserTask(int id, UserTask userTask);

//	public List<UserTask> findByStatusAndStartDateAndEndDate(List<TaskStatus> status, List<Date> startDate,
//			List<Date> endDate);

	//public List<UserTask> findByStatusAndStartDateAndEndDate(List<TaskStatus> status, Date startDate, Date endDate);

	List<UserTask> findByStatusAndStartDateAndEndDate(List<TaskStatus> status, List<Date> startDate, List<Date> endDate);

	public List<UserTaskDto> getAllUserTaskDto();

	//05-04-2023
	public List<UserTaskDto> findBySearch(String search);

	//checking
	public List<UserTask> filterUserTasks(String status, LocalDate startDate, LocalDate endDate);

}
