package com.example.todo.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class UserTask {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	//@JoinColumn(name="task_id")
	private Task task;
	
	@Enumerated(EnumType.STRING)
	private TaskStatus status = TaskStatus.TODO;
	
	private Date startDate;
	
	private Date endDate;
	
	@OneToMany(mappedBy = "usertask", cascade = CascadeType.ALL)
	private List<UserTaskHistory> userTaskHistory;

}
