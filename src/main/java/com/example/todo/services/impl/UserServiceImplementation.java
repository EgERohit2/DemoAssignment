package com.example.todo.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.todo.dto.UserDto;
import com.example.todo.entities.Role;
import com.example.todo.entities.User;
import com.example.todo.repository.RoleRepository;
import com.example.todo.repository.UserRepository;
import com.example.todo.services.UserService;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public User saveUser(User user) {
		User user1 = new User();
		user1.setEmail(user.getEmail());
		user1.setMob(user.getMob());
		user1.setUsername(user.getUsername());
		String pass = user.getPassword();
		String s = passwordEncoder.encode(pass);
		user1.setPassword(s);
		return userRepository.save(user1);
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public Optional<User> getUserById(int id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}

	@Override
	public User updateUser(int id, User user) {
		// TODO Auto-generated method stub
		User u = userRepository.findById(id).orElseThrow();
		u.setMob(user.getMob());
		u.setEmail(user.getEmail());
		String pass = user.getPassword();
		String s = passwordEncoder.encode(pass);
		u.setPassword(s);
		return u;
	}

	@Override
	public void deleteUser(int id) {
		// TODO Auto-generated method stub
		userRepository.deleteById(id);

	}

	@Override
	public void addRoles(int userId, int roleId) {
		User u = userRepository.findById(userId).orElseThrow();
		Role r = roleRepository.findById(roleId).orElseThrow();

		List<Role> userRole = u.getRole();
		userRole.add(r);
		u.setRole(userRole);
		userRepository.save(u);
	}

	@Override
	public List<UserDto> getAllUserDto() {
		List<User> u = userRepository.findAll();
		List<UserDto> userDto = new ArrayList<>();
		for (int i = 0; i < u.size(); i++) {
			UserDto ud = new UserDto();
			ud.setEmail(u.get(i).getEmail());
			ud.setMob(u.get(i).getMob());
			ud.setUsername(u.get(i).getUsername());
			ud.setPassword(u.get(i).getPassword());
			userDto.add(ud);
		}
		return userDto;
	}

	// 08-04-2023(working)
	@Override
	public UserDto getUserDtoById(int id) {
		// TODO Auto-generated method stub
		Optional<User> u = userRepository.findById(id);
		List<UserDto> userDto = new ArrayList<>();
		if (u.isPresent()) {
			User user = u.get();
			UserDto userDto2 = new UserDto();
			userDto2.setUsername(user.getUsername());
			userDto2.setMob(user.getMob());
			userDto2.setEmail(user.getEmail());
			return userDto2;
		} else {
			return null;
		}

	}

}
