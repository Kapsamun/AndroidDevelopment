package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.room;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class CelebrityEntity {

    @PrimaryKey
    public int id;

    public String name;
    public String photoUrl;
    public String biography;

    public CelebrityEntity(int id, String name, String photoUrl, String biography) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.biography = biography;
    }
}