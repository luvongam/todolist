package com.jamlech.tododemo.controller;

import com.jamlech.tododemo.entity.TodoList;
import com.jamlech.tododemo.entity.User;
import com.jamlech.tododemo.service.TodoListService;
import com.jamlech.tododemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todo")
public class TodoListController {

    private final TodoListService todoListService;
    private final  UserService userService;
    @Autowired
    public TodoListController(TodoListService todoListService, UserService userService) {
        this.todoListService = todoListService;
        this.userService = userService;
    }




    private User getCurrentUser(UserDetails userDetails){
        return userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }

    @PostMapping
    public ResponseEntity<TodoList> createToListItem(
            @RequestBody
            TodoList todoList,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = getCurrentUser(userDetails);
        return  ResponseEntity.ok(todoListService.createTodoListItem(todoList,user));
    }
    @GetMapping
    public ResponseEntity <List<TodoList>> getToListItemItems(@AuthenticationPrincipal UserDetails userDetails){
        User user = getCurrentUser(userDetails);
        return ResponseEntity.ok(todoListService.getTodoListItems(user));
    }
    @GetMapping("/{id}")
    public ResponseEntity<TodoList> getToListItemItem(
            @PathVariable Long id
    ){
        Optional<TodoList> todoList =todoListService.getTodoListItem(id);

        return todoList.map(
                ResponseEntity::ok
        ).orElseGet(()->ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<?>  updateToListItemItem(
            @PathVariable Long id,
            @RequestBody TodoList todoList,
            @AuthenticationPrincipal UserDetails userDetails
    ){
        User user = getCurrentUser(userDetails);
        return todoListService.updateTodoListItem(id,todoList,user)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<String>  deleteToListItemItem(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails

    ){
        User user = getCurrentUser(userDetails);
        boolean deleted =todoListService.deleteTodoListItem(id,user);
        return deleted ? ResponseEntity.ok("Deleted"):ResponseEntity.status(403).body(
                "Not authorized or task not found"
        );

    }

//    filter
    @GetMapping("/filter")
    public ResponseEntity<?> listToListItemFilter(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) TodoList.Status status,
            @RequestParam(required = false) TodoList.Priority priority,
            @RequestParam(required = false) String dueDate
    ){
        User user = getCurrentUser(userDetails);
        if (status != null)
            return ResponseEntity.ok(todoListService.filterTasksByStatus(user, status));
        if (priority != null)
            return ResponseEntity.ok(todoListService.filterTasksByPriority(user, priority));
        if (dueDate != null)
            return ResponseEntity.ok(todoListService.filterTasksByDueDate(user, LocalDate.parse(dueDate)));

        return ResponseEntity.badRequest().body("Provide at least one filter: status, priority, or dueDate");
    }

}
