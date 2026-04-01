package com.sushant.journalapp.scheduler;

import com.sushant.journalapp.cache.AppCache;
import com.sushant.journalapp.entity.Journal;
import com.sushant.journalapp.enums.Sentiment;
import com.sushant.journalapp.repository.UserRepositoryImpl;
import com.sushant.journalapp.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;


    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 9 ? * SUN", zone = "Asia/Kolkata")
    public void fetchUsersAndSendSaMail() {
        var users = userRepositoryImpl.getAllUsersForSentimentAnalysis();
        users.forEach(user -> {
            List<Journal> journalEntries = user.getJournals();
            List<String> filteredJournals = journalEntries.stream().filter(x ->
                    x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))
            ).map(x -> x.getContent()).toList();

            String entry = String.join(" ", filteredJournals);

            // Perform sentiment analysis
            String sentiment = Sentiment.HAPPY.toString();

            // Send email
            emailService.sendEmail(
                    user.getEmail(), "Sentiment Analysis for last 7 days",
                    "Your journal entry has been analyzed and your sentiment is: " + sentiment
            );
        });
    }

    @Scheduled(cron = "0 0/10 * 1/1 * ? ", zone = "Asia/Kolkata")
    public void clearCache() {
        appCache.initCache();
    }
}
