package com.example.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todo.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
