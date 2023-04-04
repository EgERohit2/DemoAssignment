package com.example.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.entities.Role;
import com.example.todo.services.RoleService;

@RestController
@RequestMapping("/to-do")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@PostMapping("role/data")
	public String postAllData(@RequestBody Role role) {
		roleService.postAllData(role);
		return "posted";
	}
}
