package com.sushant.journalapp.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @NonNull
    private String userName;
    @NonNull
    private String password;
    private String email;
    private boolean sentimentAnalysis;
    private List<String> roles;

    @DBRef
    private List<Journal> journals = new ArrayList<>();

    public User(ObjectId id, @NonNull String userName, @NonNull String password, String email, boolean sentimentAnalysis, List<String> roles, List<Journal> journals) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.sentimentAnalysis = sentimentAnalysis;
        this.roles = roles;
        this.journals = journals;
    }
}
