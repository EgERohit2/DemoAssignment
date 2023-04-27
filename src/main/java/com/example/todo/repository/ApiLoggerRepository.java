package com.example.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todo.entities.ApiLoggerEntity;

@Repository
public interface ApiLoggerRepository extends JpaRepository<ApiLoggerEntity, Long> {
}