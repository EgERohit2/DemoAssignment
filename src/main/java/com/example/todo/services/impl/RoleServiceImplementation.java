package com.example.todo.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo.entities.Role;
import com.example.todo.repository.RoleRepository;
import com.example.todo.services.RoleService;

@Service
public class RoleServiceImplementation implements RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role postAllData(Role role) {
		return roleRepository.save(role);
	}

}
