package com.example.todo.dto;

public class RoleDto {

	public RoleDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoleDto(String rolename) {
		super();
		this.rolename = rolename;
	}

	private String rolename;

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	@Override
	public String toString() {
		return "RoleDto [rolename=" + rolename + "]";
	}

}
