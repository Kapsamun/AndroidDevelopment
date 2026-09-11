package com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models;

public class Celebrity {
    private int id;
    private String name;
    private String photoUrl;
    private String biography;

    public Celebrity(int id, String name, String photoUrl, String biography) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.biography = biography;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getBiography() {
        return biography;
    }
}