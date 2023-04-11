package com.example.todo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todo.dto.TasksDto;
import com.example.todo.entities.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

	Page<TasksDto> findByOrderByIdDesc(Pageable pageable, Class<TasksDto> class1);

	Page<TasksDto> findByNameContainingIgnoreCaseOrderByIdDesc(String trimLeadingWhitespace, Pageable pageable,
			Class<TasksDto> class1);

	// 04-04-2023
//	List<Task> findByStatusAndStartDateBetween(TaskStatus status, LocalDate startDate, LocalDate endDate);
//
//	List<Task> findByStatusAndStartDateAfter(TaskStatus status, LocalDate startDate);
//
//	List<Task> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
//
//	List<Task> findByStatus(TaskStatus status);
//
//	List<Task> findByStartDateAfter(LocalDate startDate);
//
//	List<Task> findByStartDateBefore(LocalDate endDate);
//
//	

}
