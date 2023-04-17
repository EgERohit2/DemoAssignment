package com.example.todo.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTasksDto {

	private int userId;

	private int taskId;

	private Date startDate;

	private Date endDate;

}
