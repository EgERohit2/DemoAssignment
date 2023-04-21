package com.example.todo.dto;

import java.util.Date;

import com.example.todo.entities.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserTaskDtoAdmin {

	private TaskStatus status;
	private Date startDate;
	private Date endDate;
	private Long userId;
	private String taskName;
	
}
