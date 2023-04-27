package com.example.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todo.entities.LoggerEntity;

@Repository
public interface LoggerRepository extends JpaRepository<LoggerEntity, Long> {
	// @Query("SELECT c FROM LoggerEntity c WHERE c.token = :token")
	// LoggerEntity findByToken(@Param("token")String token);

	LoggerEntity findByToken(String token);

	void removeByToken(String token);

}
