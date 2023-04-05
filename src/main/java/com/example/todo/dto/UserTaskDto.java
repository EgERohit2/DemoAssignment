package com.example.todo.dto;

import java.util.Date;

import javax.persistence.ManyToOne;

import com.example.todo.entities.Task;
import com.example.todo.entities.TaskStatus;
import com.example.todo.entities.User;

public class UserTaskDto {

	public UserTaskDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserTaskDto(TaskStatus status, Date startDate, Date endDate, User user, Task task) {
		super();
		this.status = status;
		this.startDate = startDate;
		this.endDate = endDate;
		this.user = user;
		this.task = task;
	}

	private TaskStatus status = TaskStatus.TODO;

	private Date startDate;

	private Date endDate;

	private User user;

	private Task task;

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@Override
	public String toString() {
		return "UserTaskDto [status=" + status + ", startDate=" + startDate + ", endDate=" + endDate + ", user=" + user
				+ ", task=" + task + "]";
	}

}
