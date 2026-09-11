package com.mirea.kapsamunelenanikolaevna.movieproject.Storage.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.mirea.kapsamunelenanikolaevna.movieproject.Storage.models.Movie;

import java.time.LocalDate;

public class SharedPrefMovieStorage {
    private static final String PREFS_NAME = "movie_prefs";
    private static final String KEY_NAME = "movie_name";
    private static final String KEY_ID = "movie_id";
    private static final String KEY_DATE = "movie_date";

    private final SharedPreferences prefs;

    public SharedPrefMovieStorage(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public Movie get() {
        int id = prefs.getInt(KEY_ID, -1);
        String name = prefs.getString(KEY_NAME, null);
        String date = prefs.getString(KEY_DATE, LocalDate.now().toString());

        if (id == -1 || name == null) {
            return new Movie(1, "Доктор Стрэндж", LocalDate.now().toString());
        }
        return new Movie(id, name, date);
    }

    @Override
    public boolean save(Movie movie) {
        if (movie == null || movie.getName() == null || movie.getName().isEmpty()) {
            return false;
        }
        return prefs.edit()
                .putInt(KEY_ID, movie.getId())
                .putString(KEY_NAME, movie.getName())
                .putString(KEY_DATE, LocalDate.now().toString())
                .commit();
    }
}
}
