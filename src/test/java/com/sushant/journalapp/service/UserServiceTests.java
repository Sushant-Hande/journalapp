package com.sushant.journalapp.service;

import com.sushant.journalapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testFindByUserName() {
        assertNotNull(userService.findByUserName("Sushant"));
    }

    @ParameterizedTest
    @CsvSource({"Sushant", "Hande"})
    public void testFindByUserNameofUserService(String name){
        assertNotNull(userService.findByUserName(name));
    }

}
