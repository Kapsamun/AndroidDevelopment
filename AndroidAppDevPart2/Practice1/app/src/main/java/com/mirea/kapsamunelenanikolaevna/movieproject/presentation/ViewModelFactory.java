package com.mirea.kapsamunelenanikolaevna.movieproject.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.kapsamunelenanikolaevna.movieproject.data.repository.MovieRepositoryImpl;
import com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.MovieStorage;
import com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.sharedprefs.SharedPrefMovieStorage;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.repo.MovieRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Context context;

    public ViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        MovieStorage sharedPrefMovieStorage = new SharedPrefMovieStorage(context);
        MovieRepository movieRepository = new MovieRepositoryImpl(sharedPrefMovieStorage);
        return (T) new MainViewModel(movieRepository);
    }
}