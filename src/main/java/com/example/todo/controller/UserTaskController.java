package com.example.todo.controller;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.dto.ErrorResponseDto;
import com.example.todo.dto.SuccessResponseDto;
import com.example.todo.dto.UserTaskDto;
import com.example.todo.entities.TaskStatus;
import com.example.todo.entities.UserTask;
import com.example.todo.entities.UserTaskHistory;
import com.example.todo.repository.UserTaskRepository;
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
	private UserTaskHistoryService userTaskHistoryService;

	@PostMapping("/userTask/data")
	public ResponseEntity<?> postAllData(@RequestBody UserTask userTask) {
		try {
			userTaskService.saveUserTask(userTask);
			return new ResponseEntity<>(new SuccessResponseDto("success", "Data posted", userTask), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponseDto("Error ", "No data found", e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{id}")
	public UserTask getUserTaskById(@PathVariable("id") int id) {
		UserTask userTask = userTaskService.getUserTaskById(id).orElseThrow();
		return userTask;

	}

	//07-04-2023(WORKING-user can update his task status)
	//3.Users can update the status of the tasks - TO_DO, IN_PROGRESS and DONE
	@PutMapping("/{id}")
	public ResponseEntity<UserTask> updateUserTask(@PathVariable("id") int id, @RequestBody UserTask userTask) {
		UserTask updatedUserTask = userTaskService.updateUserTask(id, userTask);
		if (updatedUserTask != null) {
			return ResponseEntity.ok(updatedUserTask);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// not wokring
//	@PutMapping("/{id}/update-status")
//	public ResponseEntity<?> updateTaskStatus(@PathVariable("id") int id) {
//		Optional<UserTask> optionalUserTask = userTaskService.getUserTaskById(id);
//		if (optionalUserTask.isPresent()) {
//			System.out.println(optionalUserTask);
//			UserTask userTask = optionalUserTask.get();
//			Task task = userTask.getTask();
//			Date currentDate = new Date();
//			if (currentDate.after(userTask.getEndDate())) {
//				userTask.setStatus(TaskStatus.OVERDUE);
//			} else {
//				userTask.setStatus(TaskStatus.INPROGRESS);
//			}
//			userTaskService.saveUserTask(userTask);
//
//			// Add new entry in UserTaskHistory
//			UserTaskHistory userTaskHistory = new UserTaskHistory();
//			userTaskHistory.setUsertask(userTask);
//			userTaskHistory.setStatus(userTask.getStatus());
//			userTaskHistoryService.saveUserTaskHistory(userTaskHistory);
//
//			return ResponseEntity.ok("Task status updated successfully");
//		} else {
//			return ResponseEntity.notFound().build();
//		}
//	}

	// 03-04-2023
	//(if the task date is overdue then it will update the status as overdue.inprogress as per date)
	@PutMapping("/{id}/update-status")
	public ResponseEntity<?> updateTaskStatus(@PathVariable("id") int id) {
		Optional<UserTask> optionalUserTask = userTaskService.getUserTaskById(id);
		if (optionalUserTask.isPresent()) {
			UserTask userTask = optionalUserTask.get();
			if (userTask.getStatus() == TaskStatus.DONE) {
				return ResponseEntity.badRequest().body("Task has already been completed");
			}
			Date currentDate = new Date();
			if (currentDate.after(userTask.getEndDate())) {
				userTask.setStatus(TaskStatus.OVERDUE);
			} else {
				userTask.setStatus(TaskStatus.INPROGRESS);
			}
			userTaskService.saveUserTask(userTask);

			UserTaskHistory userTaskHistory = new UserTaskHistory();
			userTaskHistory.setUsertask(userTask);
			userTaskHistory.setStatus(userTask.getStatus());
			userTaskHistory.setDate(new Date());
			userTaskHistoryService.saveUserTaskHistory(userTaskHistory);

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
			return new ResponseEntity<>(new ErrorResponseDto("dataNotFound", "Data Not Found", null),
					HttpStatus.NOT_FOUND);
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
	@GetMapping("/tasks/filter")
	public List<UserTask> filterTasks(@RequestParam("status") String status,
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		return userTaskService.filterUserTasks(status, startDate, endDate);
	}

	// 05-04-2023(Working properly- based on status, Not for startDate,endDate)
	//07-04-2023(working perfectly)
//10.Users should be able to filter the tasks based on the status of the task, start date and end date.
	@GetMapping("/tasks/search")
	public List<UserTaskDto> getAllFilter(@RequestParam(value = "status",required = false) TaskStatus status,
	        @RequestParam(value = "startDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
	        @RequestParam(value = "endDate",required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

	    List<UserTaskDto> tasks = userTaskService.getAllUserTaskDto();
//		List<Object[]> tasks = userTaskService.getAllUserTask();

	    if (status != null) {
	        tasks = tasks.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList());
	    }

	    if (startDate != null && endDate != null) {
	        tasks = tasks.stream().filter(task ->
	                (task.getStartDate().compareTo(startDate) >= 0) && (task.getEndDate().compareTo(endDate) <= 0))
	                .collect(Collectors.toList());
	    } else if (startDate != null) {
	        tasks = tasks.stream().filter(task -> task.getStartDate().compareTo(startDate) >= 0)
	                .collect(Collectors.toList());
	    } else if (endDate != null) {
	        tasks = tasks.stream().filter(task -> task.getEndDate().compareTo(endDate) <= 0)
	                .collect(Collectors.toList());
	    }

	    return tasks;
	}
	
	//07-04-2023(working)
	@GetMapping("/ut/userTask")
	public List<Object[]> getAll() {
		return userTaskService.getAllUserTask();
		
	}
	
	@GetMapping("/overdue")
    public List<UserTaskDto> getAllTasks(@RequestParam(required = false) TaskStatus status,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<UserTaskDto> taskDtos = userTaskService.getAllUserTaskDto();
        
        // Filter tasks by status, start date, and end date
        if (status != null) {
            taskDtos = taskDtos.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList());
        }
        if (startDate != null) {
            taskDtos = taskDtos.stream().filter(task -> task.getStartDate().compareTo(startDate) >= 0).collect(Collectors.toList());
        }
        if (endDate != null) {
            taskDtos = taskDtos.stream().filter(task -> task.getEndDate().compareTo(endDate) <= 0).collect(Collectors.toList());
        }
        
        // Filter tasks that are overdue
        taskDtos = taskDtos.stream()
                .filter(task -> task.getStatus() == TaskStatus.INPROGRESS && task.getEndDate().compareTo(new Date()) < 0)
                .collect(Collectors.toList());
        
        return taskDtos;
    }
    
}
	
	

