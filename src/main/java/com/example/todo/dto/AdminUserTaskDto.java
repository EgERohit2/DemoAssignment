package com.example.todo.dto;

import com.example.todo.entities.TaskStatus;

public interface AdminUserTaskDto {

	public String getTaskName();
	
	public String getDescription();
	
	public TaskStatus getStatus();
	
	
}
