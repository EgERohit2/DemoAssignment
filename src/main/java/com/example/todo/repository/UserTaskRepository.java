package com.example.todo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.todo.entities.Task;
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

	// 05-04-2023(WORKING)
	@Query(value = "select status,end_date,start_date from user_task e " + "	where e.status in :status"
			+ " and e.start_date in :startDate" + "	and e.end_date in :endDate", nativeQuery = true)
	List<UserTask> findByStatusAndStartDateAndEndDate(@Param("status") List<TaskStatus> status,
			@Param("startDate") List<Date> startDate, @Param("endDate") List<Date> endDate);

	// 05-04-2023(WORKING)//not working bcz of some changes in userdto
	@Query(value = "SELECT * FROM user_task e " + "WHERE e.status LIKE %:search% " + "OR e.start_date LIKE %:search% "
			+ "OR e.end_date LIKE %:search% ", nativeQuery = true)
	List<UserTask> findBySearch(@Param("search") String search);

	// 06-04-2023(checking)
	// 07-04-2023(working)
	@Query(value = "SELECT u.user_name, u.email, u.mobile_number,t.task_name,t.description, t.created_at,ut.status , ut.start_date,ut.end_date FROM demoassignmentsecurity.user_task as ut JOIN demoassignmentsecurity.task as t ON ut.task_id = t.id JOIN demoassignmentsecurity.user as u ON ut.user_id = u.id", nativeQuery = true)
	List<Object[]> findAllUserTask();

//	@Query(value = "SELECT * FROM user_task e " + "WHERE e.status LIKE %:search% " + "OR e.start_date LIKE %:search% "
//			+ "OR e.end_date LIKE %:search% ", nativeQuery = true)
//	List<UserTaskDto> findBySearch(@Param("search") String search);

	// 05-04-2023(not working)
	List<UserTask> findByStatusAndStartDateGreaterThanEqualAndEndDateLessThanEqual(String status, Date start, Date end);

	// 07-04-2023(checking)
	@Query(value = "SELECT u.user_name, u.email, u.mobile_number,t.task_name,t.description, t.created_at,ut.status , ut.start_date,ut.end_date FROM demoassignmentsecurity.user_task as ut JOIN demoassignmentsecurity.task as t ON ut.task_id = t.id JOIN demoassignmentsecurity.user as u ON ut.user_id = u.id", nativeQuery = true)
	List<Object[]> findAllUserTaskFilt(@Param("status") List<TaskStatus> status,
			@Param("startDate") List<Date> startDate, @Param("endDate") List<Date> endDate);

	// 07-04-2023(checking)//5 pm
	@Query(value = "SELECT u.user_name, u.email, u.mobile_number, t.task_name, t.description, t.created_at, ut.status, ut.start_date, ut.end_date " +
            "FROM demoassignmentsecurity.user_task as ut " +
            "JOIN demoassignmentsecurity.task as t ON ut.task_id = t.id " +
            "JOIN demoassignmentsecurity.user as u ON ut.user_id = u.id " +
            "WHERE (:status IS NULL OR ut.status = :status) " +
            "AND (:startDate IS NULL OR ut.start_date >= :startDate) " +
            "AND (:endDate IS NULL OR ut.end_date <= :endDate) " +
            "AND (:userId IS NULL OR u.id = :userId)", nativeQuery = true)
	List<Object[]> findAllUserTask(@Param("status") TaskStatus status, 
                            @Param("startDate") Date startDate, 
                            @Param("endDate") Date endDate,  @Param("userId") int userId);
	//10-04-2023 (working)
	@Query(value =  "SELECT t.task_name, t.description, ut.status FROM user_task AS ut JOIN task AS t ON ut.task_id = t.id JOIN user AS u ON ut.user_id = u.id WHERE ut.status = 'overdue' AND u.id = ?;",nativeQuery = true)
	List<Object> findTaskByUserDtoAdmin(@Param("id") int id);

	//11-04-2023(checking) 5.05 PM
	List<UserTask> findByUserId(int id);

//	UserTask findByUserIdAndTaskId(User user, Task task);
	
}
