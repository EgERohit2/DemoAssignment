package com.example.todo.dto;

public class TaskDto {

	public TaskDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TaskDto(String name, String desc) {
		super();
		this.name = name;
		this.desc = desc;
	}

	private String name;
	private String desc;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "TaskDto [name=" + name + ", desc=" + desc + "]";
	}

}
