package com.example.todo.dto;

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
public class AddDto {

	private int taskId;
	private TaskStatus status;
}
