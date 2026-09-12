package com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo;

import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;
import java.util.List;

public interface CelebrityRepository {
    List<Celebrity> getCelebrities();
    List<Celebrity> searchCelebrities(String query);
    Celebrity getCelebrityById(int id);
    boolean addToFavorites(Celebrity celebrity);
    boolean removeFromFavorites(int celebrityId);
    List<Celebrity> getFavorites();
    String recognizeCelebrityByPhoto(String imagePath); // пока заглушка
}