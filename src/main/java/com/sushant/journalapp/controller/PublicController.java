package com.sushant.journalapp.controller;

import com.sushant.journalapp.entity.User;
import com.sushant.journalapp.service.UserService;
import com.sushant.journalapp.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Journal App is running!";
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        boolean isUserCreated = userService.saveNewUser(user);
        if (isUserCreated) {
            log.info("User Created for: {}", user.getUserName());
            return new ResponseEntity<>(isUserCreated, HttpStatus.OK);
        }
        log.error("User Creation Failed for: {}", user.getUserName());
        return new ResponseEntity<>(isUserCreated, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Login Failed for: {}", user.getUserName(), e);
            return new ResponseEntity<>("Invalid username or password", HttpStatus.BAD_REQUEST);
        }
    }


}
