package com.example.todo.dto;

import java.util.Date;

import com.example.todo.entities.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;


public interface AdminDto {


	  @JsonIgnore
	  String getName();
	  String getDescription();
	  @JsonIgnore
	  Date getStartDate();
	  @JsonIgnore
	  Date getEndDate();
	  TaskStatus getStatus();
	
	
}
