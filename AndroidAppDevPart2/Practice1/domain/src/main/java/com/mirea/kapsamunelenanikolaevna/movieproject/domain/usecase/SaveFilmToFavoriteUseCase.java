package com.mirea.kapsamunelenanikolaevna.movieproject.domain.usecase;

import com.mirea.kapsamunelenanikolaevna.movieproject.domain.models.Movie;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.repo.MovieRepository;

public class SaveFilmToFavoriteUseCase {
    private MovieRepository movieRepository;
    private Movie movie;

    public SaveFilmToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public boolean execute(Movie movie){
        return movieRepository.saveMovie(movie);
    }
}
