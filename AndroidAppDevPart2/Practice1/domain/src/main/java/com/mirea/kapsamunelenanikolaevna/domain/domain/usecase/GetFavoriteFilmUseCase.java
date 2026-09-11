package com.mirea.kapsamunelenanikolaevna.domain.domain.usecase;

import com.mirea.kapsamunelenanikolaevna.domain.domain.models.Movie;
import com.mirea.kapsamunelenanikolaevna.domain.domain.repo.MovieRepository;

public class GetFavoriteFilmUseCase {
    private final MovieRepository movieRepository;

    public GetFavoriteFilmUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie execute() {
        return movieRepository.getMovie();
    }
}
