package com.sushant.journalapp.cache;

import com.sushant.journalapp.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AppCache {

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    public Map<String, String> cache;

    @PostConstruct
    public void initCache() {
        cache = new HashMap<>();
        configJournalAppRepository.findAll().forEach(config -> {
            cache.put(config.getKey(), config.getValue());
        });

        System.out.println("App cache" + cache);
    }
}
