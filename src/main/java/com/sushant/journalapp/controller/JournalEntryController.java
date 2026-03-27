package com.sushant.journalapp.controller;

import com.sushant.journalapp.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/_journal")
public class JournalEntryController {

    private Map<Long, JournalEntry> journalentries = new HashMap<>();

    @GetMapping("/getJournalEntries")
    public List<JournalEntry> getAll(){
        return journalentries.values().stream().toList();
    }

    @PostMapping("/addJournalEntry")
    public void createEntry(@RequestBody JournalEntry entry){
        journalentries.put(entry.getId(), entry);
    }

    @GetMapping("/getJournalEntryById/{id}")
    public JournalEntry getJournalEntryById(@PathVariable Long id){
        return journalentries.get(id);
    }

    @DeleteMapping("/deleteJournalEntryById/{id}")
    public JournalEntry deleteJournalEntryById(@PathVariable Long id){
        return journalentries.remove(id);
    }

    @PutMapping("/updateJournalEntryById/{id}")
    public JournalEntry updateJournalEntryById(@PathVariable Long id, @RequestBody JournalEntry updatedEntry){
        return journalentries.put(id, updatedEntry);
    }
}
