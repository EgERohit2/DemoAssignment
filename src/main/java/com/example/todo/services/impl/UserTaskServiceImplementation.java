package com.example.todo.services.impl;

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
import com.example.todo.exceptionHandling.DataNotFoundException;
import com.example.todo.repository.UserRepository;
import com.example.todo.repository.UserTaskHistoryRepository;
import com.example.todo.repository.UserTaskRepository;
import com.example.todo.services.UserTaskService;

@Service
public class UserTaskServiceImplementation implements UserTaskService {

	@Autowired
	private UserTaskHistoryRepository userTaskHistoryRepository;

	@Autowired
	private UserTaskRepository userTaskRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserTask saveUserTask(UserTask userTask) {
		User u1 = new User();
		u1.setId(userTask.getUser().getId());

		Task t1 = new Task();
		t1.setId(userTask.getTask().getId());

		UserTask u11 = userTaskRepository.findByUserIdAndTaskId(u1.getId(), t1.getId());
			
		UserTask u12 =null;
		if (u11 != null) 
		{
			throw new DataNotFoundException("Data already Present");
		} 
		else 
		{
			u12 = userTaskRepository.save(userTask);
		}
		
		System.out.println("Hello123");
		return userTaskRepository.save(u12);
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
	public List<Object[]> getAllUserTask() {
		return userTaskRepository.findAllUserTask();
	}

	// 07-04-2023(working)
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

	@Override
	public UserTask updateTaskStatus(int id, UserTask userTask) {
		// TODO Auto-generated method stub
		Optional<UserTask> ut = userTaskRepository.findById(id);
		if (ut.isPresent()) {
			UserTask existingUserTask = ut.get();
			existingUserTask.setStatus(userTask.getStatus());

		}
		return userTask;

	}

	@Override
	public UserTask getUserTaskByUserAndTask(Integer user, Integer task) {
		// TODO Auto-generated method stub
		return userTaskRepository.findByUserIdAndTaskId(user, task);
	}

	//11-04-2023(checking) 5:00 PM
	@Override
	public UserTask updateAssignedTaskStatus(int id, UserTask userTask) {
		// TODO Auto-generated method stub
		 User u1 = new User();
		    u1.setId(userTask.getUser().getId());

		    Task t1 = new Task();
		    t1.setId(userTask.getTask().getId());

		    UserTask u11 = userTaskRepository.findByUserIdAndTaskId(u1.getId(), t1.getId());

		    if (u11 != null) {
		        throw new DataNotFoundException("Data already Present");
		    } else {
		        UserTask u12 = userTaskRepository.save(userTask);
		        System.out.println("Hello123");
		        
		        // Update task status by user_id
		        List<UserTask> userTasks = userTaskRepository.findByUserId(u1.getId());
		        for (UserTask task : userTasks) {
		            if (task.getTask().getId() == t1.getId()) {
		                task.setStatus(userTask.getStatus());
		                userTaskRepository.save(task);
		            }
		        }
		        
		        return u12;
		    }
		}

	
	//11-04-2023(working) 6:00 PM
	//3. Update user_task (note :- should be entry in usertask_history)
	@Override
	public UserTask updateTaskStatusss(int userId, int taskId, TaskStatus status) {
		// TODO Auto-generated method stub
		    UserTask userTask = userTaskRepository.findByUserIdAndTaskId(userId, taskId);
		    if (userTask == null) {
		        throw new DataNotFoundException("UserTask not found for given userId and taskId");
		    }
		    userTask.setStatus(status);
		    return userTaskRepository.save(userTask);
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

//	@Override
//	public List<Object[]> getAllUserTaskFilter() {
//		// TODO Auto-generated method stub
//		return userTaskRepository.findAllUserTaskFilt();
//	}

}
