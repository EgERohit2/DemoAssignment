package com.example.todo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.todo.dto.UserTaskDto;
import com.example.todo.entities.TaskStatus;
import com.example.todo.entities.User;
import com.example.todo.entities.UserTask;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Integer> {

	UserTask findByUserIdAndTaskId(Integer userId, Integer taskId);

	List<UserTask> findByUser(User user);

//	@Query(value = "select * from user_task e " + "	where e.status in :status" + " and e.start_date in :startDate"
//			+ "	and e.end_date in :endDate", nativeQuery = true)
//	List<UserTask> findByStatusAndStartDateAndEndDate(@Param("status") List<TaskStatus> status,
//			@Param("startDate") List<Date> startDate, @Param("endDate") List<Date> endDate);

	//05-04-2023(WORKING)
	@Query(value = "select status,end_date,start_date from user_task e " + "	where e.status in :status"
			+ " and e.start_date in :startDate" + "	and e.end_date in :endDate", nativeQuery = true)
	List<UserTask> findByStatusAndStartDateAndEndDate(@Param("status") List<TaskStatus> status,
			@Param("startDate") List<Date> startDate, @Param("endDate") List<Date> endDate);
	
	//05-04-2023(WORKING)
	@Query(value = "SELECT * FROM user_task e " + "WHERE e.status LIKE %:search% " + "OR e.start_date LIKE %:search% "
			+ "OR e.end_date LIKE %:search% ", nativeQuery = true)
	List<UserTask> findBySearch(@Param("search") String search);
	
	//06-04-2023(checking)
	@Query(value = "SELECT u.user_name, u.email, u.mobile_number,"
			+ "t.task_name,t.description, t.created_at,"
			+ "ut.status , ut.start_date,ut.end_date "
			+ "FROM demoassignmentsecurity.user_task ut "
			+ "JOIN demoassignmentsecurity.task t ON ut.task_id = t.task_id "
			+ "JOIN demoassignmentsecurity.user u ON ut.user_id = u.user_id",nativeQuery = true)
	List<UserTask> findAllUserTask();

	
//	@Query(value = "SELECT * FROM user_task e " + "WHERE e.status LIKE %:search% " + "OR e.start_date LIKE %:search% "
//			+ "OR e.end_date LIKE %:search% ", nativeQuery = true)
//	List<UserTaskDto> findBySearch(@Param("search") String search);

	//05-04-2023(not working)
	List<UserTask> findByStatusAndStartDateGreaterThanEqualAndEndDateLessThanEqual(String status, Date start, Date end);

}
