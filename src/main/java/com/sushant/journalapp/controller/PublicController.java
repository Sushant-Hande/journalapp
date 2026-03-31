package com.sushant.journalapp.controller;

import com.sushant.journalapp.entity.User;
import com.sushant.journalapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Journal App is running!";
    }

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        boolean isUserCreated = userService.saveNewUser(user);
        if (isUserCreated){
            log.info("User Created for: {}", user.getUserName());
            return new ResponseEntity<>(isUserCreated,HttpStatus.OK);
        }
        log.error("User Creation Failed for: {}", user.getUserName());
        return new ResponseEntity<>(isUserCreated, HttpStatus.OK);
    }

}
