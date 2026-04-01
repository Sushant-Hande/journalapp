package com.sushant.journalapp.entity;

import com.sushant.journalapp.enums.Sentiment;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "journal_entries")
@Data
@NoArgsConstructor
public class Journal {
    @Id
    private ObjectId id;
    private String title;
    private String content;
    private LocalDateTime date;
    private Sentiment sentiment;
}
