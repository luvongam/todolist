package com.jamlech.tododemo.service;

import com.jamlech.tododemo.entity.TodoList;
import com.jamlech.tododemo.entity.User;
import com.jamlech.tododemo.repository.TodoListRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TodoListService {
    private final TodoListRepository todoListRepository;

    public TodoListService(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

//    create
    public TodoList createTodoListItem(TodoList todoList, User user){
        todoList.setUser(user);
        return todoListRepository.save(todoList);
    }
//    get all
    public List<TodoList>  getTodoListItems(User user){
        return user.getRole()==User.Role.ADMIN ?
                todoListRepository.findAll() :
                todoListRepository.findByUser(user);
    }
//   Get Single
    public Optional<TodoList> getTodoListItem(Long id){
        return todoListRepository.findById(id);
    }
    // Update Task
    public  Optional<TodoList> updateTodoListItem(Long id,TodoList updatedTodoList,User user){
        return todoListRepository.findById(id).map(todoListItem->{
            if(user.getRole()==User.Role.ADMIN || todoListItem.getUser().getId().equals(user.getId())){
                todoListItem.setTitle(updatedTodoList.getTitle());
                todoListItem.setDescription(updatedTodoList.getDescription());
                todoListItem.setDueDate(updatedTodoList.getDueDate());
                todoListItem.setPriority(updatedTodoList.getPriority());
                todoListItem.setAttachmentType(updatedTodoList.getAttachmentType());
                return todoListRepository.save(todoListItem);
            }
            return null;
        }
        );
    }
//    delete
    public boolean deleteTodoListItem(Long id,User user){
        return todoListRepository.findById(id).map(todoListItem->{
            if(user.getRole()==User.Role.ADMIN || todoListItem.getUser().getId().equals(user.getId())){
               todoListRepository.delete(todoListItem);
               return true;
            }
            return false;
        }).orElse(false);
    }
//    update status
    public Optional<TodoList> updateTodoListItemStatus(Long id,TodoList.Status status,User user){
        return todoListRepository.findById(id).map(todoListItem->{
            if (status==TodoList.Status.ARCHIVED && user.getRole() != User.Role.ADMIN){
                return null;
            }
            if (user.getRole() == User.Role.ADMIN || todoListItem.getUser().getId().equals(user.getId())){
                todoListItem.setStatus(status);
                return  todoListRepository.save(todoListItem);
            }
            return null;
        });

    }
    // Filter and Sort Tasks (Basic Implementation)
    public List<TodoList> filterTasksByStatus(User user, TodoList.Status status) {
        return todoListRepository.findByUserAndStatus(user, status);
    }

    public List<TodoList> filterTasksByPriority(User user, TodoList.Priority priority) {
        return todoListRepository.findByUserAndPriority(user, priority);
    }

    public List<TodoList> filterTasksByDueDate(User user, LocalDate dueDate) {
        return todoListRepository.findByUserAndDueDate(user, dueDate);
    }

}
