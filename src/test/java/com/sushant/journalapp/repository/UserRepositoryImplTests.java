package com.sushant.journalapp.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryImplTests {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    public void testGetAllUsersForSentimentAnalysis() {
        System.out.println("Testing getAllUsersForSentimentAnalysis");
        var users = userRepositoryImpl.getAllUsersForSentimentAnalysis();
        System.out.println("Users for sentiment analysis: "+users);
    }

}
