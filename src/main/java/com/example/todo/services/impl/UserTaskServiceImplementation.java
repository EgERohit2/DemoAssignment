package com.example.todo.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.entities.TaskStatus;
import com.example.todo.entities.UserTask;
import com.example.todo.entities.UserTaskHistory;
import com.example.todo.repository.UserTaskHistoryRepository;
import com.example.todo.repository.UserTaskRepository;
import com.example.todo.services.UserTaskService;

@Service
public class UserTaskServiceImplementation implements UserTaskService {

	@Autowired
	private UserTaskHistoryRepository userTaskHistoryRepository;

	@Autowired
	private UserTaskRepository userTaskRepository;

	@Override
	public UserTask saveUserTask(UserTask userTask) {
		// TODO Auto-generated method stub
		return userTaskRepository.save(userTask);
	}

	@Override
	public List<UserTask> getAllUserTasks() {
		// TODO Auto-generated method stub
		return userTaskRepository.findAll();
	}

	@Override
	public Optional<UserTask> getUserTaskById(int id) {
		// TODO Auto-generated method stub
		return userTaskRepository.findById(id);
	}

	@Override
	public UserTask updateUserTask(int id, UserTask userTask) {
		Optional<UserTask> optionalUserTask = userTaskRepository.findById(id);
		if (optionalUserTask.isPresent()) {
			UserTask existingUserTask = optionalUserTask.get();
			existingUserTask.setUser(userTask.getUser());
			existingUserTask.setTask(userTask.getTask());
			existingUserTask.setStatus(userTask.getStatus());
			existingUserTask.setUserTaskHistory(userTask.getUserTaskHistory());

			UserTask u = userTaskRepository.save(existingUserTask);

			UserTask u1 = userTaskRepository.findByUserIdAndTaskId(userTask.getUser().getId(),
					existingUserTask.getTask().getId());

			UserTask uId = new UserTask();
			uId.setId(u1.getId());

			UserTask st = new UserTask();
			st.setStatus(u1.getStatus());

			UserTaskHistory u11 = new UserTaskHistory();
			u11.setUsertask(uId);
			u11.setStatus(st.getStatus());

			userTaskHistoryRepository.save(u11);

			return u;
		} else {
			return null;
		}
	}

	@Override
	public List<UserTask> findByStatusAndStartDateAndEndDate(List<TaskStatus> status, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return userTaskRepository.findByStatusAndStartDateAndEndDate(status,startDate,endDate);
	}

}
