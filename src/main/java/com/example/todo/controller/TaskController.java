package com.example.todo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.component.JwtUtil;
import com.example.todo.dto.ErrorResponseDto;
import com.example.todo.dto.SuccessResponseDto;
import com.example.todo.dto.TaskDto;
import com.example.todo.dto.TasksDto;
import com.example.todo.entities.Task;
import com.example.todo.entities.User;
import com.example.todo.entities.UserTask;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.repository.UserTaskRepository;
import com.example.todo.services.TaskService;

@RestController
@RequestMapping("/to-do")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserTaskRepository userTaskRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TaskRepository taskRepository;

	@PostMapping("task/data")
	public ResponseEntity<?> postAllData(@RequestBody Task task) {
		try {
			taskService.saveTask(task);
			return new ResponseEntity<>(new SuccessResponseDto("success", "Data posted", null), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponseDto("Error ", "No data found", e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("task")
	public List<Task> getAllData() {
		return taskService.getAllTasks();
	}

	// Admin can see all the list of tasks.
	// 4.When a user fetches the list of tasks he/she should be able to see only
	// those tasks that are assigned to him/her. Admin can see all the list of
	// tasks.
	@PreAuthorize("hasRole('ROLE_admin')")
	@GetMapping("taskDto/getAllDto")
	public List<TaskDto> getAllTaskDto() {
		return taskService.getAllTaskDto();
	}

	@PutMapping("update/task")
	public ResponseEntity<?> updateTask(@RequestParam(value = "task_id") int id, @RequestBody Task task) {
		try {
			taskService.updateTask(id, task);
			return new ResponseEntity<>(new SuccessResponseDto("success", "Data updated", null), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponseDto("Error ", "No data found", e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("taskDto/data")
	public TaskDto updateTaskById(@RequestParam(value = "id") int id) {
		TaskDto t = taskService.getTaskDtoById(id);
		return t;
	}

	// 04-04-2023
	@GetMapping("/tasksDto/pagination")
	public ResponseEntity<?> getAllPagination(@RequestParam(value = "search") String search,
			@RequestParam(value = "pageNumber") Integer pageNumber,
			@RequestParam(value = "pageSize") Integer pageSize) {

		List<TasksDto> cvs = taskService.getAllwithDto(search, pageNumber, pageSize);
		return new ResponseEntity<>(cvs, HttpStatus.OK);
	}

	// 04-04-2023(working)
	// 4.When a user fetches the list of tasks he/she should be able to see only
	// those
	// tasks that are assigned to him/her. Admin can see all the list of tasks
//	@GetMapping("/tasks")
//	public ResponseEntity<?> getAllTasksForUser(@RequestHeader("Authorization") String token) {
//		String username = JwtUtil.parseToken(token.replace("Bearer ", ""));
//
//		User user = userRepository.findByUsername(username);
//
//		List<TaskDto> taskDtos = new ArrayList<>();
//		List<UserTask> userTasks = userTaskRepository.findByUser(user);
//
//		for (UserTask userTask : userTasks) {
//			Task task = userTask.getTask();
//			TaskDto taskDto = new TaskDto(task.getName(), task.getDesc());
//			taskDtos.add(taskDto);
//		}
//
//		return ResponseEntity.ok(taskDtos);
//	}

	// 10-04-2023(working) 5.45pm
	// 4.When a user fetches the list of tasks he/she should be able to see only
	// those
	// tasks that are assigned to him/her.
	// 11.Users can create overdue tasks such that they can set the start date of
	// the
	// tasks in future. These tasks should only be visible only when the start date
	// has passed.
	@GetMapping("/tasks")
	public ResponseEntity<List<TaskDto>> getAllTasksAssignedToUser(@RequestHeader("Authorization") String token) {
		String username = JwtUtil.parseToken(token.replace("Bearer ", ""));
		User user = userRepository.findByUsername(username);
		List<TaskDto> taskDtos = new ArrayList<>();
		Date currentDate = new Date();

		List<UserTask> userTasks = userTaskRepository.findByUser(user);
		for (UserTask userTask : userTasks) {
			Task task = userTask.getTask();
			Date taskStartDate = userTask.getStartDate();
			if (taskStartDate != null && currentDate.after(taskStartDate)) {
				TaskDto taskDto = new TaskDto(task.getName(), task.getDesc());
				taskDtos.add(taskDto);
			}
		}

		return ResponseEntity.ok(taskDtos);
	}

	// 07-04-2023(working)
	// Users do not have the permission to delete a task. Only Admin can delete a
	// task.
	@PreAuthorize("hasRole('ROLE_admin')")
	@DeleteMapping("task/delete/{id}")
	public ResponseEntity<?> deleteTask(@PathVariable int id) {
		try {
			taskRepository.deleteById(id);
			return new ResponseEntity<>(new SuccessResponseDto("success", "deleted", null), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponseDto("Error ", "No data found", e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}

	}
}
