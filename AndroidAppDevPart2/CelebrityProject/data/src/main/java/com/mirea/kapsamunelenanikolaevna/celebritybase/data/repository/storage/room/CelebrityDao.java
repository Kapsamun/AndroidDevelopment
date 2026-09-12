package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CelebrityDao {

    @Query("SELECT * FROM favorites")
    List<CelebrityEntity> getAll();

    @Query("SELECT * FROM favorites WHERE id = :id LIMIT 1")
    CelebrityEntity getById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CelebrityEntity celebrity);

    @Query("DELETE FROM favorites WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    boolean isFavorite(int id);
}