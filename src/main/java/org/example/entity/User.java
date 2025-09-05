package org.example.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class User {
    private UUID userId;
    private String name;
    private String number;
    private int creditRating;

    public User(){
        this.userId = UUID.randomUUID();
    }

    public User(String name, String number, int creditRating){
        this();
        this.name = name;
        this.number = number;
        this.creditRating = creditRating;
    }
}
