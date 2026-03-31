package com.sushant.journalapp.service;

import com.sushant.journalapp.entity.Journal;
import com.sushant.journalapp.entity.User;
import com.sushant.journalapp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public void saveJournalEntry(Journal journal, String userName) {
        try {
            User user = userService.findByUserName(userName);
            journal.setDate(LocalDateTime.now());
            Journal journalSaved = journalEntryRepository.save(journal);
            user.getJournals().add(journalSaved);
            userService.saveUser(user);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while saving entry", e);
        }
    }

    public void updateJournalEntry(Journal journal) {
        journalEntryRepository.save(journal);
    }

    public List<Journal> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<Journal> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            removed = user.getJournals().removeIf(journal -> journal.getId().equals(id));
            if (removed) {
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("An error occurred while deleting entry", e);
        }
        return removed;
    }
}
