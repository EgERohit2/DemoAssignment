package com.example.todo.dto;

import java.util.Date;

import com.example.todo.entities.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskDto {



//	public UserTaskDto(TaskStatus status, Date startDate, Date endDate, User user, Task task) {
//		super();
//		this.status = status;
//		this.startDate = startDate;
//		this.endDate = endDate;
//		this.user = user;
//		this.task = task;
//	}

	private TaskStatus status = TaskStatus.TODO;

	private Date startDate;

	private Date endDate;

	private String user;

	private String task;

	 

}
