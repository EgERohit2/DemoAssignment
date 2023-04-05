package com.example.todo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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

	//Admin can see all the list of tasks.
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

	// 04-04-2023
	@GetMapping("/tasks")
	public ResponseEntity<?> getAllTasksForUser(@RequestHeader("Authorization") String token) {
		String username = JwtUtil.parseToken(token.replace("Bearer ", ""));

		User user = userRepository.findByUsername(username);

		List<TaskDto> taskDtos = new ArrayList<>();
		List<UserTask> userTasks = userTaskRepository.findByUser(user);

		for (UserTask userTask : userTasks) {
			Task task = userTask.getTask();
			TaskDto taskDto = new TaskDto(task.getName(), task.getDesc());
			taskDtos.add(taskDto);
		}

		return ResponseEntity.ok(taskDtos);
	}
	
	//04-04-2023 4.01pm (not working)
//	@GetMapping("/tasks")
//	public ResponseEntity<List<TaskDto>> getTasksByStatusAndDate(
//	        @RequestParam(value = "status", required = false) TaskStatus status,
//	        @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//	        @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//
//	    List<Task> tasks;
//	    if (status != null && startDate != null && endDate != null) {
//	        tasks = taskRepository.findByStatusAndStartDateBetween(status, startDate, endDate);
//	    } else if (status != null && startDate != null) {
//	        tasks = taskRepository.findByStatusAndStartDateAfter(status, startDate);
//	    } else if (status != null && endDate != null) {
//	        tasks = taskRepository.findByStatusAndStartDateAfter(status, endDate);
//	    } else if (startDate != null && endDate != null) {
//	        tasks = taskRepository.findByStartDateBetween(startDate, endDate);
//	    } else if (status != null) {
//	        tasks = taskRepository.findByStatus(status);
//	    } else if (startDate != null) {
//	        tasks = taskRepository.findByStartDateAfter(startDate);
//	    } else if (endDate != null) {
//	        tasks = taskRepository.findByStartDateBefore(endDate);
//	    } else {
//	        tasks = taskRepository.findAll();
//	    }
//
//	    List<TaskDto> taskDtos = tasks.stream().map(TaskDto::new).collect(Collectors.toList());
//
//	    return ResponseEntity.ok(taskDtos);
//	}

}
