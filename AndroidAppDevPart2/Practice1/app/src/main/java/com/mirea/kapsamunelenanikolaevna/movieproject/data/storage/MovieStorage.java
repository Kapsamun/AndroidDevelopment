package com.mirea.kapsamunelenanikolaevna.movieproject.data.storage;

import com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.models.Movie;

public interface MovieStorage {
    Movie get();
    boolean save(Movie movie);
}