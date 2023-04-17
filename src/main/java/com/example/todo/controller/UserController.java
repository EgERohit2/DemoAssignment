package com.example.todo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.todo.dto.UserDto;
import com.example.todo.entities.Task;
import com.example.todo.entities.User;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.services.TaskService;
import com.example.todo.services.UserService;
import com.example.todo.services.UserTaskService;

@RestController
@RequestMapping("/to-do")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserTaskService userTaskService;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskService taskService;

	@PostMapping("/user/data")
	public ResponseEntity<?> postAllData(@RequestBody User user) {
		try {
			userService.saveUser(user);
			return new ResponseEntity<>(new SuccessResponseDto("success", "done", null), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponseDto("Error ", "No data found"), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/user")
	public List<User> getAllData() {
		return userService.getAllUsers();
	}

	@GetMapping("user/data/{id}")
	public void getDataById(@RequestParam(value = "id") int id, @RequestBody User user) {
		userService.getUserById(id);
	}

	@DeleteMapping("user/data/delete/{id}")
	public ResponseEntity<?> deleteDataById(@RequestParam(value = "id") int id) {
		try {
			userService.deleteUser(id);
			return new ResponseEntity<>(new SuccessResponseDto("success", "deleted", null), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponseDto("Error ", "No data found"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/post/assignRoles")
	public ResponseEntity<?> post(@RequestParam(value = "user_id") int user_id,
			@RequestParam(value = "role_id") int role_id) {
		try {
			this.userService.addRoles(user_id, role_id);
			return new ResponseEntity<>(new SuccessResponseDto("success", "roles assigned", null), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponseDto("Error ", "No data found"),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/userDto/getAllDto")
	public List<UserDto> getAllDto() {
		return userService.getAllUserDto();
	}

	// 04-04-2023

//	@PutMapping("/task/update/{id}")
//	public ResponseEntity<?> updateTaskById(@PathVariable("id") int id, @RequestBody Task t,
//			@RequestHeader("Authorization") String token) {
//
//		// Extract username from token
//		String username = JwtUtil.parseToken(token.replace("Bearer ", ""));
//
//		// Fetch user by username
//		User user = userRepository.findByUsername(username);
//		System.out.println(user);
//		if (user.getId() != id && user.getRole() != Role.admin && user.getRole() != Role.employee) {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//		}
//		// Fetch task by ID
//		Optional<Task> optionalTask = taskService.getTaskById(id);
//		if (optionalTask.isPresent()) {
//			System.out.println(optionalTask);
//			Task task = optionalTask.get();
//			TaskDto taskDto = new TaskDto();
//			task.setName(t.getName());
//			task.setDesc(t.getDesc());
//
//			// Save updated task
//			taskService.saveTask(task);
//			return ResponseEntity.ok(taskDto);
//		} else {
//			return ResponseEntity.notFound().build();
//		}
//
//	}

	// 11-04-2023(checking) 11.00 AM
	// Id is passing it should be user_id which and as per this that users assigns
	// task should be upadted(expected)
	// It is taking task_id of task table (current situation)
	@PutMapping("/task/update/{id}")
	public ResponseEntity<?> updateTaskById(@PathVariable("id") int id, @RequestBody TaskDto taskDto,
			@RequestHeader("Authorization") String token) {

		try {

			// Extract username from token
			String username = JwtUtil.parseToken(token.replace("Bearer ", ""));

			// Fetch user by username
			User user = userRepository.findByUsername(username);
			System.out.println(user);

//	 Try to use this in if statement --  user.getRole().contains("admin")||user.getRole().contains("employee")
			// if the users role is not an admin or an employee then it return unauthorized.
			if (!user.getRole().contains("admin") || !user.getRole().contains("employee")) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
			// Fetch task by ID
			Optional<Task> optionalTask = taskService.getTaskById(id);
			if (optionalTask.isPresent()) {
				System.out.println(optionalTask);
				Task task = optionalTask.get();
				task.setName(taskDto.getName());
				task.setDesc(taskDto.getDesc());
//	        task.setStatus(taskDto.getStatus());

				// Save updated task
				taskService.saveTask(task);
				return ResponseEntity.ok(taskDto);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.ok(e.getMessage());
		}

	}

	// 07-04-2023(working)
	// 5.Status of the task can be updated only by the assigned user and admin.
	// Users
	// cannot update the task status that is not assigned to them
//	@PutMapping("/task/update/auth/{id}")
//	public ResponseEntity<?> updateTaskByIdAuth(@PathVariable("id") int id, @RequestBody Task task,
//			@RequestHeader("Authorization") String token) {
//
//		try {
//
//			// Extract username from token
//			String username = JwtUtil.parseToken(token.replace("Bearer ", ""));
//
//			// Fetch user by username
//			User user = userRepository.findByUsername(username);
//			System.out.println(user);
//
////	 Try to use this in if statement --  user.getRole().contains("admin")||user.getRole().contains("employee")
//			// if the users role is not an admin or an employee then it return unauthorized.
//			if (user.getRole().contains("admin") || user.getRole().contains("employee")) {
//				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//			}
//			// Fetch task by ID
//			TaskDto optionalTask = taskService.getTaskDtoById(id);
//			if (optionalTask != null) {
//				System.out.println(optionalTask);
//				TaskDto taskDto = new TaskDto();
//				task.setName(taskDto.getName());
//				task.setDesc(taskDto.getDesc());
////	        task.setStatus(taskDto.getStatus());
//
//				// Save updated task
//				taskRepository.save(task);
//				return ResponseEntity.ok(taskDto);
//			} else {
//				return ResponseEntity.notFound().build();
//			}
//		} catch (Exception e) {
//			return ResponseEntity.ok(e.getMessage());
//		}
//
//	}

	// 11-04-2023(not working) 11.20 AM
//	@PutMapping("/task/update")
//	public ResponseEntity<?> updateTaskByUserTaskId(@RequestBody TaskDto taskDto, @RequestParam("userId") int userId, @RequestHeader("Authorization") String token) {
//	    try {
//	        // Extract username from token
//	        String username = JwtUtil.parseToken(token.replace("Bearer ", ""));
//	        // Fetch user by username
//	        User user = userRepository.findByUsername(username);
//
//	        // Check if the user role is not admin or employee, return unauthorized
//	        if (!user.getRole().contains("admin") && !user.getRole().contains("employee")) {
//	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//	        }
//
//	        // Fetch UserTask by ID
//	        Optional<UserTask> optionalUserTask = userTaskService.getUserTaskById(taskDto.getUserTaskId());
//	        if (optionalUserTask.isPresent()) {
//	            UserTask userTask = optionalUserTask.get();
//	            
//	            // Check if the UserTask belongs to the specified user ID
//	            if (userTask.getUser().getId() != userId) {
//	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//	            }
//	            
//	            Task task = userTask.getTask();
//	            task.setName(taskDto.getName());
//	            task.setDesc(taskDto.getDesc());
//	            // Update the task status if needed
//	            // task.setStatus(taskDto.getStatus());
//
//	            // Save updated task
//	            taskService.saveTask(task);
//	            return ResponseEntity.ok(taskDto);
//	        } else {
//	            return ResponseEntity.notFound().build();
//	        }
//	    } catch (Exception e) {
//	        return ResponseEntity.ok(e.getMessage());
//	    }
//	}

	// 08-04-2023(working)
	@GetMapping("/userDto/{id}")
	public UserDto getUserDtoById(@PathVariable int id) {
		return this.userService.getUserDtoById(id);
	}

}
