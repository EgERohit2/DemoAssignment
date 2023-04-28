package com.example.todo.services;

import java.util.List;
import java.util.Optional;

import com.example.todo.dto.UserDto;
import com.example.todo.dto.UserTaskDto;
import com.example.todo.entities.User;

public interface UserService {

	public User saveUser(User user);

	public List<User> getAllUsers();

	public List<UserDto> getAllUserDto();

	public Optional<User> getUserById(int id);

	public User updateUser(int id, User user);

	public void deleteUser(int id);

	public void addRoles(int userId, int roleId);
	
	//08-04-2023(checking)
	public UserDto getUserDtoById(int id);
	
	//21-04-2023(working) 10.30am
	public void deleteUserById(int id);


	


}
