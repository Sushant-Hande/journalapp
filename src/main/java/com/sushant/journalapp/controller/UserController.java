package com.sushant.journalapp.controller;

import com.sushant.journalapp.entity.User;
import com.sushant.journalapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping("/createUser")
    public void createUser( @RequestBody User user) {
        userService.saveUser(user);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        User existingUser = userService.findByUserName(user.getUserName());
        if (existingUser != null) {
            existingUser.setPassword(user.getPassword());
            userService.saveUser(existingUser);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
