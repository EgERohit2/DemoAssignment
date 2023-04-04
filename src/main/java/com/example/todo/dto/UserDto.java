package com.example.todo.dto;

public class UserDto {

	public UserDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDto(String username, String email, String mob, String password) {
		super();
		this.username = username;
		this.email = email;
		this.mob = mob;
		this.password = password;
	}

	private String username;
	private String email;
	private String mob;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMob() {
		return mob;
	}

	public void setMob(String mob) {
		this.mob = mob;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserDto [username=" + username + ", email=" + email + ", mob=" + mob + ", password=" + password + "]";
	}

}
