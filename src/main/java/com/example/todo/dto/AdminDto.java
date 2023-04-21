package com.example.todo.dto;

import java.util.Date;

import com.example.todo.entities.TaskStatus;

public interface AdminDto {


	public Date getStartDate();
	
	public Date getEndDate();
	
	public TaskStatus getStatus();
	
	public int getUserId();
	
	public int getTaskId();
	
	
}
