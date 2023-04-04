package com.example.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todo.entities.UserTask;
import com.example.todo.entities.UserTaskHistory;

@Repository
public interface UserTaskHistoryRepository extends JpaRepository<UserTaskHistory, Integer> {

}
