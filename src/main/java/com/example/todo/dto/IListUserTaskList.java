package com.example.todo.dto;

import com.example.todo.entities.TaskStatus;

public interface IListUserTaskList {

	public String getStartDate();
	
	public String getEndDate();
	
	public TaskStatus getStatus();
}
