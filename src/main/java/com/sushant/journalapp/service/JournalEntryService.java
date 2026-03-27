package com.sushant.journalapp.service;

import com.sushant.journalapp.entity.Journal;
import com.sushant.journalapp.entity.User;
import com.sushant.journalapp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public void saveJournalEntry(Journal journal, String userName) {
        User user = userService.findByUserName(userName);
        journal.setDate(LocalDateTime.now());
        Journal journalSaved = journalEntryRepository.save(journal);
        user.getJournals().add(journalSaved);
        userService.saveUser(user);
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

    public void deleteById(ObjectId id, String userName) {
        User user = userService.findByUserName(userName);
        user.getJournals().removeIf(journal -> journal.getId().equals(id));
        userService.saveUser(user);
        journalEntryRepository.deleteById(id);
    }
}
