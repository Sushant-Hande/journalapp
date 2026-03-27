package com.sushant.journalapp.controller;

import com.sushant.journalapp.entity.Journal;
import com.sushant.journalapp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("/getJournalEntries")
    public ResponseEntity<List<Journal>> getAll() {
        List<Journal> journals = journalEntryService.getAll();
        if (journals != null && !journals.isEmpty()) {
            return new ResponseEntity<>(journals, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addJournalEntry")
    public ResponseEntity<Journal> createEntry(@RequestBody Journal journal) {
        try {
            journal.setDate(LocalDateTime.now());
            journalEntryService.saveJournalEntry(journal);
            return new ResponseEntity<>(journal, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getJournalEntryById/{id}")
    public ResponseEntity<Journal> getJournalEntryById(@PathVariable ObjectId id) {
        Optional<Journal> journal = journalEntryService.findById(id);
        if (journal.isPresent()) {
            return new ResponseEntity<>(journal.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteJournalEntryById/{id}")
    public boolean deleteJournalEntryById(@PathVariable ObjectId id) {
        journalEntryService.deleteById(id);
        return true;
    }

    @PutMapping("/updateJournalEntryById/{id}")
    public ResponseEntity<Journal> updateJournalEntryById(@PathVariable ObjectId id, @RequestBody Journal updatedEntry) {
        Journal journal = journalEntryService.findById(id).orElse(null);
        if (journal != null) {
            journal.setTitle(updatedEntry.getTitle() != null && !updatedEntry.getTitle().isEmpty() ? updatedEntry.getTitle() : journal.getTitle());
            journal.setContent(updatedEntry.getContent() != null && !updatedEntry.getContent().isEmpty() ? updatedEntry.getContent() : journal.getContent());
            journalEntryService.saveJournalEntry(journal);
            return new ResponseEntity<>(journal, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
