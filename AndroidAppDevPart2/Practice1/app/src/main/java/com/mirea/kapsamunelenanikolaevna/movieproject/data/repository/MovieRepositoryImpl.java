package com.mirea.kapsamunelenanikolaevna.movieproject.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.mirea.kapsamunelenanikolaevna.movieproject.domain.models.Movie;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.repo.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {

    private static final String PREFS_NAME = "movie_prefs";
    private static final String KEY_MOVIE_ID = "movie_id";
    private static final String KEY_MOVIE_NAME = "movie_name";

    private final SharedPreferences preferences;

    public MovieRepositoryImpl(Context context) {
        // Context используется только здесь, в Data-слое
        this.preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean saveMovie(Movie movie) {
        if (movie == null || movie.getName() == null || movie.getName().isEmpty()) {
            return false;
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_MOVIE_ID, movie.getId());
        editor.putString(KEY_MOVIE_NAME, movie.getName());
        return editor.commit(); // commit() возвращает true/false
    }

    @Override
    public Movie getMovie() {
        int id = preferences.getInt(KEY_MOVIE_ID, -1);
        String name = preferences.getString(KEY_MOVIE_NAME, null);

        if (id == -1 || name == null) {
            // Если ничего не сохранено — возвращаем значение по умолчанию
            return new Movie(1, "Доктор Стрэндж");
        }

        return new Movie(id, name);
    }
}