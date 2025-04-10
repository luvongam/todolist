package com.jamlech.tododemo.controller;

import com.jamlech.tododemo.entity.User;
import com.jamlech.tododemo.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Getter
@Setter
public class UserController {
    private UserService userService;

//    public UserController(UserService userService) {
//        this.userService = userService;
//    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }
    @PostMapping("/login")
    public Optional<User> login(@RequestBody User user) {
        return userService.getUserByUsername(user.getUserName());
    }
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Long id,) {
//
//    }

}
