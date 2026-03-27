package com.sushant.journalapp.service;

import com.sushant.journalapp.entity.Journal;
import com.sushant.journalapp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public void saveJournalEntry(Journal journal) {
         journalEntryRepository.save(journal);
    }

    public List<Journal> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<Journal> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        journalEntryRepository.deleteById(id);
    }
}
