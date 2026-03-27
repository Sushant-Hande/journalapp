package com.sushant.journalapp.controller;

import com.sushant.journalapp.entity.Journal;
import com.sushant.journalapp.entity.User;
import com.sushant.journalapp.service.JournalEntryService;
import com.sushant.journalapp.service.UserService;
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

    @Autowired
    private UserService userService;

    @GetMapping("/getJournalEntries/{userName}")
    public ResponseEntity<List<Journal>> getAllJournalEntriesOfUser(@PathVariable String userName) {
        User user = userService.findByUserName(userName);
        List<Journal> journals = user.getJournals();
        if (journals != null && !journals.isEmpty()) {
            return new ResponseEntity<>(journals, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addJournalEntry/{userName}")
    public ResponseEntity<Journal> createEntry(@RequestBody Journal journal, @PathVariable String userName) {
        try {
            journalEntryService.saveJournalEntry(journal, userName);
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

    @DeleteMapping("/deleteJournalEntryById/{userName}/{id}")
    public boolean deleteJournalEntryById(@PathVariable ObjectId id, @PathVariable String userName) {
        journalEntryService.deleteById(id, userName);
        return true;
    }

    @PutMapping("/updateJournalEntryById/{userName}/{id}")
    public ResponseEntity<Journal> updateJournalEntryById(
            @PathVariable ObjectId id,
            @RequestBody Journal updatedEntry,
            @PathVariable String userName) {
        Journal journal = journalEntryService.findById(id).orElse(null);
        if (journal != null) {
            journal.setTitle(updatedEntry.getTitle() != null && !updatedEntry.getTitle().isEmpty() ? updatedEntry.getTitle() : journal.getTitle());
            journal.setContent(updatedEntry.getContent() != null && !updatedEntry.getContent().isEmpty() ? updatedEntry.getContent() : journal.getContent());
            journalEntryService.updateJournalEntry(journal);
            return new ResponseEntity<>(journal, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
