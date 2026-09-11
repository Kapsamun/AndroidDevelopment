package com.mirea.kapsamunelenanikolaevna.domain.domain.repo;

import com.mirea.kapsamunelenanikolaevna.domain.domain.models.Movie;

public interface MovieRepository {
    boolean saveMovie(Movie movie);
    Movie getMovie();
}
