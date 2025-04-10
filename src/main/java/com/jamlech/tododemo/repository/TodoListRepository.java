package com.jamlech.tododemo.repository;

import com.jamlech.tododemo.entity.TodoList;
import com.jamlech.tododemo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface  TodoListRepository extends JpaRepository<TodoList,Long> {
    List<TodoList> findByUser(User user);

    List<TodoList> findByUserAndStatus(User user, TodoList.Status status);

    List<TodoList> findByUserAndPriority(User user, TodoList.Priority priority);

    List<TodoList> findByUserAndDueDate(User user, LocalDate dueDate);
}
