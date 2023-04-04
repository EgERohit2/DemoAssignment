package com.example.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todo.entities.ErrorLoggerEntity;

@Repository
public interface ErrorloggerRepository extends JpaRepository<ErrorLoggerEntity, Integer>{

}
