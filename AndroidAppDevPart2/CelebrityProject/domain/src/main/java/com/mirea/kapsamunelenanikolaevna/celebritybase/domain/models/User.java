package com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models;

public class User {
    private final String uid;
    private final String email;

    public User(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }
}