package com.mirea.kapsamunelenanikolaevna.movieproject.data.repository;

import com.mirea.kapsamunelenanikolaevna.movieproject.Storage.models.Movie;
import com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.MovieStorage;

public class MovieRepositoryImpl implements MovieRepository {

    private final MovieStorage storage;

    public MovieRepositoryImpl(MovieStorage storage) {
        this.storage = storage;
    }

    @Override
    public boolean saveMovie(Movie movie) {
        com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.models.Movie dataMovie =
                new com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.models.Movie(
                        movie.getId(),
                        movie.getName(),
                        java.time.LocalDate.now().toString()
                );
        return storage.save(dataMovie);
    }

    @Override
    public Movie getMovie() {
        com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.models.Movie dataMovie = storage.get();
        return new Movie(dataMovie.getId(), dataMovie.getName());
    }
}