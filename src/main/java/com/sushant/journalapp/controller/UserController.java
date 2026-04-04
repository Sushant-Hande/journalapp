package com.sushant.journalapp.controller;

import com.sushant.journalapp.apiresponse.WeatherResponse;
import com.sushant.journalapp.entity.User;
import com.sushant.journalapp.service.RedisService;
import com.sushant.journalapp.service.UserService;
import com.sushant.journalapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private RedisService redisService;

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User existingUser = userService.findByUserName(userName);
        if (existingUser != null) {
            existingUser.setPassword(user.getPassword());
            userService.saveNewUser(existingUser);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User existingUser = userService.findByUserName(userName);
        if (existingUser != null) {
            userService.deleteById(existingUser.getId());
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getWeather")
    ResponseEntity<?> getWeather() {
        String city = "Mumbai";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        WeatherResponse cachedWeatherResponse = redisService.get(city, WeatherResponse.class);
        if (cachedWeatherResponse != null) {
            return new ResponseEntity<>(cachedWeatherResponse, HttpStatus.OK);
        }else {
            WeatherResponse  weatherResponse= weatherService.getWeather(city);
            String grreting = "";
            if (weatherResponse !=null){
                redisService.set(city, weatherResponse, 300L);
                grreting = " Weather Feels like " + weatherResponse.getCurrent().getFeelslike();
            }
            return new ResponseEntity<>("Hi " + userName + grreting, HttpStatus.OK);
        }
    }

}
