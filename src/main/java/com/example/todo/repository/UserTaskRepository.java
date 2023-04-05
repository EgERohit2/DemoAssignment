package com.example.todo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.todo.entities.TaskStatus;
import com.example.todo.entities.User;
import com.example.todo.entities.UserTask;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Integer> {

	UserTask findByUserIdAndTaskId(Integer userId, Integer taskId);

	List<UserTask> findByUser(User user);


	@Query(value = "select * from userTask e " + "	where e.department_id in :departmentId"
			+ " and e.branch_id in :branchId" + "	and e.designation_id in :designationId", nativeQuery = true)
	List<UserTask> findByStatusAndStartDateAndEndDate(List<TaskStatus> status, Date startDate, Date endDate);
	
	
}
