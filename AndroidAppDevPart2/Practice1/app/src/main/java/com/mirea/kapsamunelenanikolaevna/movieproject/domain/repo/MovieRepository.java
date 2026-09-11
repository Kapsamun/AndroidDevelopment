package com.mirea.kapsamunelenanikolaevna.movieproject.domain.repo;

import com.mirea.kapsamunelenanikolaevna.movieproject.domain.models.Movie;

public interface MovieRepository {
    public boolean saveMovie(Movie movie);
    public Movie getMovie();

}