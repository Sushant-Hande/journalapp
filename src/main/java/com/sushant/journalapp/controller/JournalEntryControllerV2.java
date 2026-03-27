package com.sushant.journalapp.controller;

import com.sushant.journalapp.entity.Journal;
import com.sushant.journalapp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping("/getJournalEntries")
    public List<Journal> getAll() {
        return journalEntryService.getAll();
    }

    @PostMapping("/addJournalEntry")
    public Journal createEntry(@RequestBody Journal journal) {
        journal.setDate(LocalDateTime.now());
        journalEntryService.saveJournalEntry(journal);
        return journal;
    }

    @GetMapping("/getJournalEntryById/{id}")
    public Journal getJournalEntryById(@PathVariable ObjectId id) {
        return journalEntryService.findById(id).orElse(null);
    }

    @DeleteMapping("/deleteJournalEntryById/{id}")
    public boolean deleteJournalEntryById(@PathVariable ObjectId id) {
        journalEntryService.deleteById(id);
        return true;
    }

    @PutMapping("/updateJournalEntryById/{id}")
    public Journal updateJournalEntryById(@PathVariable ObjectId id, @RequestBody Journal updatedEntry) {
        Journal journal = journalEntryService.findById(id).orElse(null);
        if (journal!=null){
        journal.setTitle(updatedEntry.getTitle()!=null && !updatedEntry.getTitle().isEmpty() ?updatedEntry.getTitle():journal.getTitle());
        journal.setContent(updatedEntry.getContent()!=null && !updatedEntry.getContent().isEmpty() ?updatedEntry.getContent():journal.getContent());
        }
        journalEntryService.saveJournalEntry(journal);
        return journal;
    }
}
