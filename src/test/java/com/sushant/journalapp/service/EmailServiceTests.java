package com.sushant.journalapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendEmail() {
    emailService.sendEmail("sushanthande93@gmail.com", "Test Email", "Hi Sushant, \n This is a test email.");
    }

}
