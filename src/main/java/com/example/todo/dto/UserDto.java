package com.example.todo.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	@NotNull(message = "username cannot be null")
	private String username;
	@NotNull(message = "email cannot be an empty")
	private String email;
	@Size(min=0,max=10,message = "phone number is not valid")
	private String mob;
	@NotNull(message = "password cannot be an empty")
	private String password;
}
