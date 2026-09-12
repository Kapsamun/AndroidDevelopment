package com.mirea.kapsamunelenanikolaevna.movieproject.domain.usecase;

import com.mirea.kapsamunelenanikolaevna.movieproject.domain.models.Movie;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.repo.MovieRepository;

public class GetFavoriteFilmUseCase {
    private MovieRepository movieRepository;
    public GetFavoriteFilmUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public Movie execute(){
        return movieRepository.getMovie();
    }
}