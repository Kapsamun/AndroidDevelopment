package com.mirea.kapsamunelenanikolaevna.domain.domain.usecase;

import com.mirea.kapsamunelenanikolaevna.domain.domain.models.Movie;
import com.mirea.kapsamunelenanikolaevna.domain.domain.repo.MovieRepository;

public class SaveFilmToFavoriteUseCase {
    private final MovieRepository movieRepository;

    public SaveFilmToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public boolean execute(Movie movie) {
        return movieRepository.saveMovie(movie);
    }
}
