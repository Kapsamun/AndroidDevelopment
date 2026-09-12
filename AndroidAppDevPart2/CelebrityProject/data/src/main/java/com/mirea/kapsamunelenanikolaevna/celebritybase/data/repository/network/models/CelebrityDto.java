package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models;

public class CelebrityDto {
    public int id;
    public String name;
    public String photoUrl;
    public String biography;

    public CelebrityDto(int id, String name, String photoUrl, String biography) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.biography = biography;
    }
}