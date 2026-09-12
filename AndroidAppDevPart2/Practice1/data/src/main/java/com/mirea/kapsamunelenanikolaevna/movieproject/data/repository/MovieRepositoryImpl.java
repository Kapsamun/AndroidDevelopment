package com.mirea.kapsamunelenanikolaevna.movieproject.data.repository;

import com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.MovieStorage;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.models.Movie;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.repo.MovieRepository;

import java.time.LocalDate;

public class MovieRepositoryImpl implements MovieRepository {

    private final MovieStorage storage;

    public MovieRepositoryImpl(MovieStorage storage) {
        this.storage = storage;
    }

    @Override
    public boolean saveMovie(Movie movie) {
        // Domain → Data
        com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.models.Movie dataMovie =
                mapToStorage(movie);
        return storage.save(dataMovie);
    }

    @Override
    public Movie getMovie() {
        // Data → Domain
        com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.models.Movie dataMovie =
                storage.get();
        return mapToDomain(dataMovie);
    }

    private com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.models.Movie mapToStorage(Movie movie) {
        return new com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.models.Movie(
                movie.getId(),
                movie.getName(),
                LocalDate.now().toString()
        );
    }

    private Movie mapToDomain(com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.models.Movie dataMovie) {
        return new Movie(dataMovie.getId(), dataMovie.getName());
    }
}