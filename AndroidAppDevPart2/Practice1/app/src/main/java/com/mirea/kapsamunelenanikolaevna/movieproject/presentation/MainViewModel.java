package com.mirea.kapsamunelenanikolaevna.movieproject.presentation;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mirea.kapsamunelenanikolaevna.movieproject.domain.models.Movie;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.repo.MovieRepository;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.usecase.GetFavoriteFilmUseCase;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.usecase.SaveFilmToFavoriteUseCase;

public class MainViewModel extends ViewModel {

    private final MovieRepository movieRepository;
    private final MutableLiveData<String> favoriteMovie = new MutableLiveData<>();

    public MainViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        Log.d(MainViewModel.class.getSimpleName(), "MainViewModel created");
    }

    public MutableLiveData<String> getFavoriteMovie() {
        return favoriteMovie;
    }

    public void setText(Movie movie) {
        Boolean result = new SaveFilmToFavoriteUseCase(movieRepository).execute(movie);
        favoriteMovie.setValue(result.toString());
    }

    public void getText() {
        Movie movie = new GetFavoriteFilmUseCase(movieRepository).execute();
        favoriteMovie.setValue(String.format("My favorite movie is %s", movie.getName()));
    }

    @Override
    protected void onCleared() {
        Log.d(MainViewModel.class.getSimpleName(), "MainViewModel cleared");
        super.onCleared();
    }
}