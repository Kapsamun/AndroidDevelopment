package com.mirea.kapsamunelenanikolaevna.movieproject.presentation;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mirea.kapsamunelenanikolaevna.movieproject.R;
import com.mirea.kapsamunelenanikolaevna.movieproject.data.repository.MovieRepositoryImpl;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.repo.MovieRepository;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.usecase.GetFavoriteFilmUseCase;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.usecase.SaveFilmToFavoriteUseCase;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.models.Movie;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText text = findViewById(R.id.editTextMovie);
        TextView textView = findViewById(R.id.textViewMovie);
        MovieRepository movieRepository = new MovieRepositoryImpl(this);

        findViewById(R.id.buttonSaveMovie).setOnClickListener(view -> {
            Boolean result = new SaveFilmToFavoriteUseCase(movieRepository)
                    .execute(new Movie(2, text.getText().toString()));
            textView.setText("Save result: " + result);
        });

        findViewById(R.id.buttonGetMovie).setOnClickListener(view -> {
            Movie movie = new GetFavoriteFilmUseCase(movieRepository).execute();
            textView.setText("Favorite movie: " + movie.getName());
        });
    }}