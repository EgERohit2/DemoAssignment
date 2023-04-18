package com.example.todo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.todo.dto.UserTaskDto;
import com.example.todo.dto.UserTasksDto;
import com.example.todo.entities.Task;
import com.example.todo.entities.TaskStatus;
import com.example.todo.entities.User;
import com.example.todo.entities.UserTask;
import com.example.todo.entities.UserTaskHistory;
import com.example.todo.exceptionHandling.DataNotFoundException;
import com.example.todo.repository.RoleRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.repository.UserTaskRepository;
import com.example.todo.services.TaskService;
import com.example.todo.services.UserService;
import com.example.todo.services.UserTaskHistoryService;
import com.example.todo.services.UserTaskService;

@RestController
@RequestMapping("/to-do")
public class UserTaskController {

	@Autowired
	private UserTaskRepository userTaskRepository;

	@Autowired
	private UserTaskService userTaskService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserTaskHistoryService userTaskHistoryService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserService userService;

	@PostMapping("/AssignUserTask")
	public ResponseEntity<?> postAllData(@RequestBody UserTasksDto userTask) {
		try {
			userTaskService.saveUserTask(userTask);
			return new ResponseEntity<>(new SuccessResponseDto("success", "Data posted", userTask), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponseDto(e.getMessage(), "userNotFound"), HttpStatus.BAD_REQUEST);
		}
	}

//	@PostMapping("/userTask/data")
//	public ResponseEntity<?> postAllData(@RequestBody UserTask userTask) {
//	    try {
//	        // Check if the user is already assigned to the task
//	        UserTask existingUserTask = userTaskService.getUserTaskById(userTask.getId()).orElseThrow();
//	        if (existingUserTask != null) {
//	            // User is already assigned to the task, return error response
//	            return new ResponseEntity<>(new ErrorResponseDto("Error", "User is already assigned to the task",
//	                    "User with ID " + userTask.getUser().getId() + " is already assigned to Task with ID " + userTask.getTask().getId()),
//	                    HttpStatus.BAD_REQUEST);
//	        } else {
//	            // User is not assigned to the task, save the UserTask data
//	            userTaskService.saveUserTask(userTask);
//	            return new ResponseEntity<>(new SuccessResponseDto("Success", "Data posted", userTask), HttpStatus.CREATED);
//	        }
//	    } catch (Exception e) {
//	        return new ResponseEntity<>(new ErrorResponseDto("Error", "No data found", e.getMessage()),
//	                HttpStatus.BAD_REQUEST);
//	    }
//	}

	@GetMapping("/{id}")
	public UserTask getUserTaskById(@PathVariable("id") int id) {
		UserTask userTask = userTaskService.getUserTaskById(id).orElseThrow();
		return userTask;

	}

	// 07-04-2023(WORKING-user can update his task status)
	// 3.Users can update the status of the tasks - TO_DO, IN_PROGRESS and DONE
	@PutMapping("/{id}")
	public ResponseEntity<UserTask> updateUserTask(@PathVariable("id") int id, @RequestBody UserTask userTask) {
		UserTask updatedUserTask = userTaskService.updateUserTask(id, userTask);
		if (updatedUserTask != null) {
			return ResponseEntity.ok(updatedUserTask);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// 10-04-2023(working) 6.50 PM
	// (if the task date is overdue then it will update the status as
	// overdue.inprogress as per date)
	@PutMapping("/{id}/update-status")
	public ResponseEntity<?> updateTaskStatus(@PathVariable("id") int taskId) {
		Optional<Task> optionalTask = taskService.getTaskById(taskId);
		if (optionalTask.isPresent()) {
			Task task = optionalTask.get();
			List<UserTask> userTasks = task.getUsertask();
			for (UserTask userTask : userTasks) {
				if (userTask.getStatus() == TaskStatus.DONE) {
					return ResponseEntity.badRequest().body("Task has already been completed by some users");
				}
				Date currentDate = new Date();
				if (currentDate.after(userTask.getEndDate())) {
					userTask.setStatus(TaskStatus.OVERDUE);
				}
//				else {
//					userTask.setStatus(TaskStatus.INPROGRESS);
//				}  (this is not necessary)
				userTaskService.savesUserTask(userTask);

				UserTaskHistory userTaskHistory = new UserTaskHistory();
				userTaskHistory.setUsertask(userTask);
				userTaskHistory.setStatus(userTask.getStatus());
				userTaskHistory.setDate(new Date());
				userTaskHistoryService.saveUserTaskHistory(userTaskHistory);
			}

			return ResponseEntity.ok("Task status updated successfully");
		} else {
			return ResponseEntity.notFound().build();
		}
	}

//	@GetMapping("/filter")
//	public ResponseEntity<?> getTaskByFilter(@RequestParam(value = "status", required = true) List<TaskStatus> status,
//			@RequestParam(value = "startDate", required = true) List<Date> startDate,
//			@RequestParam(value = "endDate", required = true) List<Date> endDate) {
//
//		List<UserTask> database = userTaskRepository.findAll();
//		if (database.isEmpty()) {
//			try {
//				List<UserTask> userTask = userTaskService.findByStatusAndStartDateAndEndDate(status, startDate,
//						endDate);
//
//				if (userTask != null && !userTask.isEmpty()) {
//					return new ResponseEntity<>(new SuccessResponseDto("success", "success", database), HttpStatus.OK);
//				} else {
//					return new ResponseEntity<>(new ErrorResponseDto("dataNotFound", "Data Not Found", null),
//							HttpStatus.NOT_FOUND);
//				}
//			} catch (Exception e) {
//
//				return new ResponseEntity<>(new ErrorResponseDto("Something Went Wrong", e.getMessage(), null),
//						HttpStatus.NOT_ACCEPTABLE);
//			}
//		} else {
//			return new ResponseEntity<>(new ErrorResponseDto("dataNotFound", "Data Not Found", null),
//					HttpStatus.NOT_FOUND);
//		}
//	}
	// 05-04-2023(not working)
	@GetMapping("/filter")
	public ResponseEntity<?> getTaskByFilter(@RequestParam(value = "status", required = false) List<TaskStatus> status,
			@RequestParam(value = "startDate", required = false) List<Date> startDate,
			@RequestParam(value = "endDate", required = false) List<Date> endDate) {

		List<UserTask> userTasks = userTaskService.findByStatusAndStartDateAndEndDate(status, startDate, endDate);

		if (!userTasks.isEmpty()) {
			return new ResponseEntity<>(new SuccessResponseDto("success", "success", userTasks), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ErrorResponseDto("usernot", "userNotFound"), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/userTask/data")
	public List<UserTaskDto> getAllUserTask() {
		return this.userTaskService.getAllUserTaskDto();

	}

	// 05-04-2023(Working)
	@GetMapping("/search")
	public List<UserTask> getllSearch(@RequestParam(value = "search", required = false) String search) {
		return userTaskRepository.findBySearch(search);

	}

//	@GetMapping("/search/dto")
//	public List<UserTaskDto> getllSearchDto(@RequestParam (value="search",required = false)String search){
//		return userTaskService.findBySearch(search);
//		//return userTaskRepository.findBySearch(search);
//		
//	}

	// checking 4.17pm (not working)
//	@GetMapping("/tasks/filter")
//	public List<UserTask> filterTasks(@RequestParam("status") String status,
//			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//		return userTaskService.filterUserTasks(status, startDate, endDate);
//	}

	// 05-04-2023(Working properly- based on status, Not for startDate,endDate)
	// 07-04-2023(working perfectly)
//10.Users should be able to filter the tasks based on the status of the task, start date and end date.
//	@GetMapping("/tasks/search")
//	public List<UserTaskDto> getAllFilter(@RequestParam(value = "status", required = false) TaskStatus status,
//			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
//			@RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
//
//		List<UserTaskDto> tasks = userTaskService.getAllUserTaskDto();
////		List<Object[]> tasks = userTaskService.getAllUserTask();
//
//		if (status != null) {
//			tasks = tasks.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList());
//		}
//
//		if (startDate != null && endDate != null) {
//			tasks = tasks.stream().filter(task -> (task.getStartDate().compareTo(startDate) >= 0)
//					&& (task.getEndDate().compareTo(endDate) <= 0)).collect(Collectors.toList());
//		} else if (startDate != null) {
//			tasks = tasks.stream().filter(task -> task.getStartDate().compareTo(startDate) >= 0)
//					.collect(Collectors.toList());
//		} else if (endDate != null) {
//			tasks = tasks.stream().filter(task -> task.getEndDate().compareTo(endDate) <= 0)
//					.collect(Collectors.toList());
//		}
//
//		return tasks;
//	}

	// checking 9.35 am(working partially)
	@GetMapping("/tasks/search")
	public ResponseEntity<?> getAllFilter(@RequestParam(value = "status", required = false) TaskStatus status,
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestHeader("Authorization") String token) {

		// Extract username from token
		String username = JwtUtil.parseToken(token.replace("Bearer ", ""));

		// Fetch user by username
		User user = userRepository.findByUsername(username);

		System.out.println(user.getId());

		// take role id from user
		Long s = userTaskRepository.roleId(user.getId());
		System.out.println(s);
		// get rolename from roleId
		String rolen = roleRepository.name(s);

		// if it is equal then it will continue with rest of the code
		if (user != null && rolen.equals("admin")) {
			List<UserTaskDto> tasks = userTaskService.getAllUserTaskDto();

			if (status != null) {
				tasks = tasks.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList());
			}

			else if (startDate != null) {
				tasks = tasks.stream().filter(task -> task.getStartDate().compareTo(startDate) >= 0)
						.collect(Collectors.toList());
			} else if (endDate != null) {
				tasks = tasks.stream().filter(task -> task.getEndDate().compareTo(endDate) <= 0)
						.collect(Collectors.toList());
			}

			return ResponseEntity.ok(tasks);
		}

		else if (user != null && rolen.equals("employee")) {
			List<UserTask> taskss = userTaskRepository.findByUser(user);
			System.out.println("@@@@@" + user);
			System.out.println("&&&&&" + taskss);
			if (status != null) {
				taskss = taskss.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList());
			} else if (startDate != null) {
				taskss = taskss.stream().filter(task -> task.getStartDate().compareTo(startDate) >= 0)
						.collect(Collectors.toList());
			} else if (endDate != null) {
				taskss = taskss.stream().filter(task -> task.getEndDate().compareTo(endDate) <= 0)
						.collect(Collectors.toList());
			}

			return ResponseEntity.ok(taskss);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	// 18-04-2023(checking)-(working-4.00pm)
	@GetMapping("/tests")
	public ResponseEntity<?> getTasks(@RequestParam(value = "status", required = false) TaskStatus status,
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "assigned", required = false, defaultValue = "false") boolean assigned,
			@RequestHeader("Authorization") String token) {

		String username = JwtUtil.parseToken(token.replace("Bearer ", ""));
		User user = userRepository.findByUsername(username);

		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String roleName = roleRepository.name(userTaskRepository.roleId(user.getId()));

		if (roleName.equals("admin")) {
			List<UserTaskDto> tasks = userTaskService.getAllUserTaskDto();

			if (status != null) {
				tasks = tasks.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList());
			}

			if (startDate != null) {
				tasks = tasks.stream().filter(task -> task.getStartDate().compareTo(startDate) >= 0)
						.collect(Collectors.toList());
			}

			if (endDate != null) {
				tasks = tasks.stream().filter(task -> task.getEndDate().compareTo(endDate) <= 0)
						.collect(Collectors.toList());
			}

			return ResponseEntity.ok(tasks);
		} else if (roleName.equals("employee")) {
			List<TaskDto> taskDtos = new ArrayList<>();
			Date currentDate = new Date();

			List<UserTask> userTasks = userTaskRepository.findByUser(user);

			if (assigned) {
				for (UserTask userTask : userTasks) {
					Task task = userTask.getTask();
					Date taskStartDate = userTask.getStartDate();
					if (taskStartDate != null && currentDate.after(taskStartDate)) {
						TaskDto taskDto = new TaskDto(task.getName(), task.getDesc());
						taskDtos.add(taskDto);
					}
				}
			} else {
				List<UserTask> tasks = userTasks;

				if (status != null) {
					tasks = tasks.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList());
				}

				if (startDate != null) {
					tasks = tasks.stream().filter(task -> task.getStartDate().compareTo(startDate) >= 0)
							.collect(Collectors.toList());
				}

				if (endDate != null) {
					tasks = tasks.stream().filter(task -> task.getEndDate().compareTo(endDate) <= 0)
							.collect(Collectors.toList());
				}

				taskDtos = tasks.stream()
						.map(userTask -> new TaskDto(userTask.getTask().getName(), userTask.getTask().getDesc()))
						.collect(Collectors.toList());
			}

			return ResponseEntity.ok(taskDtos);
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	// 07-04-2023(working)
	@GetMapping("/ut/userTask")
	public List<Object[]> getAll() {
		return userTaskService.getAllUserTask();

	}

	// 08-04-2023(working)
	@GetMapping("/overdue")
	public List<UserTaskDto> getAllTasks(@RequestParam(value = "status", required = false) TaskStatus status) {
		List<UserTaskDto> taskDtos = userTaskService.getAllUserTaskDto();

		// Filter tasks by status, start date, and end date
		if (status != null) {
			taskDtos = taskDtos.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList());
		}

		// Filter tasks that are overdue
		taskDtos = taskDtos.stream().filter(task -> task.getStatus() == TaskStatus.OVERDUE)
				.collect(Collectors.toList());

		return taskDtos;
	}

	// 08-04-2023(working)
//		Admin should be able to filter the tasks based on the user.
	@GetMapping("/tasks/search/admin")
	public List<UserTaskDto> getAllFilterAdminOnly(@RequestParam(value = "user", required = false) String user) {

		List<UserTaskDto> tasks = userTaskService.getAllUserTaskDto();

		User u = userRepository.findByUsername(user);
//		List<Object[]> tasks = userTaskService.getAllUserTask();

		if (u != null) {
			tasks = tasks.stream().filter(task -> task.getUser().toString() == u.getUsername().toString())
					.collect(Collectors.toList());
		}

		return tasks;
	}

	// 08-04-2023 (cheking) 2.10pm
	@GetMapping("/overdue/{id}")
	public List<UserTaskDto> getAllTasksOverD(@PathVariable int id, @RequestHeader("Authorization") String token) {

		String username = JwtUtil.parseToken(token.replace("Bearer ", ""));

		User user = userRepository.findByUsername(username);

		List<UserTaskDto> taskDtos = userTaskService.getAllUserTaskDto();

		User u = userService.getUserById(id).orElseThrow();
		// Filter tasks by status, start date, and end date
		if (u != null) {

		}

		return taskDtos;
	}

	// 10-04-2023(working)
	// 10.Users should also have the ability to view overdue tasks. That is the
	// tasks that are past the completion date
	@GetMapping("/overdue/admin")
	public ResponseEntity<Object> getAllOverdueTasksForUser(@RequestParam(value = "id", required = false) int id,
			@RequestHeader("Authorization") String token) {
		try {
			// Extract username from token
			String username = JwtUtil.parseToken(token.replace("Bearer ", ""));

			// Fetch user by username
			User user = userRepository.findByUsername(username);
			System.out.println(user);

			if (user.getId() != id && user.getRole().contains("admin") || user.getRole().contains("employee")) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			} else {
				List<Object> overdueTasks = userTaskRepository.findTaskByUserDtoAdmin(id);
				if (overdueTasks != null) {
					return ResponseEntity.ok(overdueTasks);
				} else {
					return ResponseEntity.notFound().build();
				}
			}
		} catch (Exception e) {
			return ResponseEntity.ok(e.getMessage());
		}
	}

	@PutMapping("update/taskstatus/{id}")
	public String updateTaskStatus(@PathVariable int id, @RequestBody UserTask userTask) {
		this.userTaskService.updateAssignedTaskStatus(id, userTask);
		return "updated";
	}

	// 11-04-2023(working)
	@PutMapping("/userTask/data")
	public ResponseEntity<?> updateTaskStatus(@RequestBody UserTask userTask) {
		try {
			UserTask updatedUserTask = userTaskService.updateTaskStatusss(userTask.getUser().getId(),
					userTask.getTask().getId(), userTask.getStatus());
			return new ResponseEntity<>(new SuccessResponseDto("success", "Task status updated", updatedUserTask),
					HttpStatus.OK);
		} catch (DataNotFoundException e) {
			return new ResponseEntity<>(new ErrorResponseDto("Error", "Data not found"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponseDto("Error", "Failed to update task status"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// checking(18-04-2023)
	@GetMapping("/tests12")
	public ResponseEntity<?> getTasks(@RequestParam(value = "status", required = false) TaskStatus status,
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "assigned", required = false, defaultValue = "false") boolean assigned,
			@RequestParam(value = "user", required = false) String user, @RequestHeader("Authorization") String token) {

		String username = JwtUtil.parseToken(token.replace("Bearer ", ""));
		User user1 = userRepository.findByUsername(username);

		if (user1 == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String roleName = roleRepository.name(userTaskRepository.roleId(user1.getId()));

		if (roleName.equals("admin")) {
			List<UserTaskDto> tasks = userTaskService.getAllUserTaskDto();

			if (user1 != null) {
				tasks = tasks.stream().filter(task -> task.getUser().toString().equals(user1))
						.collect(Collectors.toList());
			}

			if (status != null) {
				tasks = tasks.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList());
			}

			if (startDate != null) {
				tasks = tasks.stream().filter(task -> task.getStartDate().compareTo(startDate) >= 0)
						.collect(Collectors.toList());
			}

			if (endDate != null) {
				tasks = tasks.stream().filter(task -> task.getEndDate().compareTo(endDate) <= 0)
						.collect(Collectors.toList());
			}

			return ResponseEntity.ok(tasks);
		} else if (roleName.equals("employee")) {
			List<TaskDto> taskDtos = new ArrayList<>();
			Date currentDate = new Date();

			List<UserTask> userTasks = userTaskRepository.findByUser(user1);

			if (assigned) {
				for (UserTask userTask : userTasks) {
					Task task = userTask.getTask();
					Date taskStartDate = userTask.getStartDate();
					if (taskStartDate != null && currentDate.after(taskStartDate)) {
						TaskDto taskDto = new TaskDto(task.getName(), task.getDesc());
						taskDtos.add(taskDto);
					}
				}
			} else {
				List<UserTask> tasks = userTasks;

				if (status != null) {
					tasks = tasks.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList());
				}

				if (startDate != null) {
					tasks = tasks.stream().filter(task -> task.getStartDate().compareTo(startDate) >= 0)
							.collect(Collectors.toList());
				}

				if (endDate != null) {
					tasks = tasks.stream().filter(task -> task.getEndDate().compareTo(endDate) <= 0)
							.collect(Collectors.toList());
				}

				taskDtos = tasks.stream()
						.map(userTask -> new TaskDto(userTask.getTask().getName(), userTask.getTask().getDesc()))
						.collect(Collectors.toList());
			}

			return ResponseEntity.ok(taskDtos);
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@GetMapping("/tasks/test2")
	public ResponseEntity<?> getTasks(@RequestParam(value = "status", required = false) TaskStatus status,
			@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "assigned", required = false, defaultValue = "false") boolean assigned,
			@RequestParam(value = "search") String search, @RequestParam(value = "pageNumber") Integer pageNumber,
			@RequestParam(value = "pageSize") Integer pageSize, @RequestHeader("Authorization") String token) {

		String username = JwtUtil.parseToken(token.replace("Bearer ", ""));
		User user = userRepository.findByUsername(username);

		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String roleName = roleRepository.name(userTaskRepository.roleId(user.getId()));

		if (roleName.equals("admin")) {
			List<UserTaskDto> tasks = userTaskService.getAllUserTaskDto();

			if (status != null) {
				tasks = tasks.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList());
			}

			if (startDate != null) {
				tasks = tasks.stream().filter(task -> task.getStartDate().compareTo(startDate) >= 0)
						.collect(Collectors.toList());
			}

			if (endDate != null) {
				tasks = tasks.stream().filter(task -> task.getEndDate().compareTo(endDate) <= 0)
						.collect(Collectors.toList());
			}

			List<TasksDto> cvs = taskService.getAllwithDto(search, pageNumber, pageSize);
			return new ResponseEntity<>(cvs, HttpStatus.OK);
		} else if (roleName.equals("employee")) {
			List<TaskDto> taskDtos = new ArrayList<>();
			Date currentDate = new Date();

			List<UserTask> userTasks = userTaskRepository.findByUser(user);

			if (assigned) {
				for (UserTask userTask : userTasks) {
					Task task = userTask.getTask();
					Date taskStartDate = userTask.getStartDate();
					if (taskStartDate != null && currentDate.after(taskStartDate)) {
						TaskDto taskDto = new TaskDto(task.getName(), task.getDesc());
						taskDtos.add(taskDto);
					}
				}
			} else {
				List<UserTask> tasks = userTasks;

				if (status != null) {
					tasks = tasks.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList());
				}

				if (startDate != null) {
					tasks = tasks.stream().filter(task -> task.getStartDate().compareTo(startDate) >= 0)
							.collect(Collectors.toList());
				}

				if (endDate != null) {
					tasks = tasks.stream().filter(task -> task.getEndDate().compareTo(endDate) <= 0)
							.collect(Collectors.toList());
				}

				taskDtos = tasks.stream()
						.map(userTask -> new TaskDto(userTask.getTask().getName(), userTask.getTask().getDesc()))
						.collect(Collectors.toList());
			}

			List<TasksDto> cvs = taskService.getAllwithDto(search, pageNumber, pageSize);
			return new ResponseEntity<>(cvs, HttpStatus.OK);
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

}
