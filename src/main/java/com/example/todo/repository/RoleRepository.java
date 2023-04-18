package com.example.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.todo.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

	@Query(value= "select rolename from role where id=?",nativeQuery = true)
	String name (@Param("id")Long id);
}
