package com.mirea.kapsamunelenanikolaevna.movieproject.presentation;

import android.graphics.Movie;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mirea.kapsamunelenanikolaevna.movieproject.R;
import com.mirea.kapsamunelenanikolaevna.movieproject.data.repository.MovieRepositoryImpl;
import com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.MovieStorage;
import com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.sharedprefs.SharedPrefMovieStorage;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.repo.MovieRepository;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.usecase.GetFavoriteFilmUseCase;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.usecase.SaveFilmToFavoriteUseCase;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText text = findViewById(R.id.editTextMovie);
        TextView textView = findViewById(R.id.textViewMovie);

        MovieStorage storage = new SharedPrefMovieStorage(this);
        MovieRepository movieRepository = new MovieRepositoryImpl(storage);

        findViewById(R.id.buttonSaveMovie).setOnClickListener(view -> {
            boolean result = new SaveFilmToFavoriteUseCase(movieRepository)
                    .execute(new com.mirea.kapsamunelenanikolaevna.movieproject.domain.models.Movie(
                            2, text.getText().toString()));
            textView.setText("Save result: " + result);
        });

        findViewById(R.id.buttonGetMovie).setOnClickListener(view -> {
            com.mirea.kapsamunelenanikolaevna.movieproject.domain.models.Movie movie =
                    new GetFavoriteFilmUseCase(movieRepository).execute();
            textView.setText("Favorite movie: " + movie.getName());
        });
    }
}