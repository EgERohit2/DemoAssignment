package com.example.todo.services.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.dto.UserTaskDto;
import com.example.todo.entities.Task;
import com.example.todo.entities.TaskStatus;
import com.example.todo.entities.User;
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
	public List<UserTask> findByStatusAndStartDateAndEndDate(List<TaskStatus> status, List<Date> startDate,
			List<Date> endDate) {
		// TODO Auto-generated method stub
		return userTaskRepository.findByStatusAndStartDateAndEndDate(status, startDate, endDate);
	}
	
	@Override
	public List<Object[]> getAllUserTask(){
		return userTaskRepository.findAllUserTask();
	}

	//07-04-2023(working)
	@Override
	public List<UserTaskDto> getAllUserTaskDto() {
		// TODO Auto-generated method stub
		List<UserTask> ut = userTaskRepository.findAll();
		List<UserTaskDto> udto = new ArrayList<>();
		for (int i = 0; i < ut.size(); i++) {
			UserTaskDto userTaskDto = new UserTaskDto();
			userTaskDto.setStatus(ut.get(i).getStatus());
			userTaskDto.setStartDate(ut.get(i).getStartDate());
			userTaskDto.setEndDate(ut.get(i).getEndDate());
//			userTaskDto.setTask(ut.get(i).getTask());
//			userTaskDto.setUser(ut.get(i).getUser());
			
			Task ut1 = new Task();
			ut1.setName(ut.get(i).getTask().getName());
			
			userTaskDto.setTask(ut1.getName());
			
			User ut2 = new User();
			ut2.setUsername(ut.get(i).getUser().getUsername());
			
			userTaskDto.setUser(ut2.getUsername());
			
			udto.add(userTaskDto);
		}
		return udto;
	}

	// 05-04-2023
//	@Override
//	public List<UserTaskDto> findBySearch(String search) {
//		// TODO Auto-generated method stub
//		List<UserTask> ut = userTaskRepository.findAll();
//		List<UserTaskDto> udto = new ArrayList<>();
//		for (int i = 0; i < ut.size(); i++) {
//			UserTaskDto userTaskDto = new UserTaskDto();
//			userTaskDto.setStatus(ut.get(i).getStatus());
//			userTaskDto.setStartDate(ut.get(i).getStartDate());
//			userTaskDto.setEndDate(ut.get(i).getEndDate());
//			userTaskDto.setTask(ut.get(i).getTask());
//			userTaskDto.setUser(ut.get(i).getUser());
//			udto.add(userTaskDto);
//
//		}
//		return udto;
//	}

	//checking (not-working)
	@Override
	public List<UserTask> filterUserTasks(String status, LocalDate startDate, LocalDate endDate) {
		// TODO Auto-generated method stub
		Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return userTaskRepository.findByStatusAndStartDateGreaterThanEqualAndEndDateLessThanEqual(status, start, end);
	}

//	@Override
//	public List<Object[]> getAllUserTaskFilter() {
//		// TODO Auto-generated method stub
//		return userTaskRepository.findAllUserTaskFilt();
//	}

}
