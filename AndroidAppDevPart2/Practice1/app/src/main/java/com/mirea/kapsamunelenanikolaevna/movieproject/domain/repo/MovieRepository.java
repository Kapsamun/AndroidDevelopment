package com.mirea.kapsamunelenanikolaevna.movieproject.domain.repo;

import com.mirea.kapsamunelenanikolaevna.movieproject.domain.models.Movie;

public interface MovieRepository {
    boolean saveMovie(Movie movie);
    Movie getMovie();
}