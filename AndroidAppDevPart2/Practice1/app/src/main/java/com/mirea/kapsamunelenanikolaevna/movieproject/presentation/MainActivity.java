package com.mirea.kapsamunelenanikolaevna.movieproject.presentation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.kapsamunelenanikolaevna.movieproject.R;
import com.mirea.kapsamunelenanikolaevna.movieproject.data.repository.MovieRepositoryImpl;
import com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.MovieStorage;
import com.mirea.kapsamunelenanikolaevna.movieproject.data.storage.sharedprefs.SharedPrefMovieStorage;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.models.Movie;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.repo.MovieRepository;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.usecase.GetFavoriteFilmUseCase;
import com.mirea.kapsamunelenanikolaevna.movieproject.domain.usecase.SaveFilmToFavoriteUseCase;


public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(MainActivity.class.getSimpleName(), "MainActivity created");

        // Инициализация ViewModel через фабрику
        mainViewModel = new ViewModelProvider(this, new ViewModelFactory(this)).get(MainViewModel.class);

        TextView textView = findViewById(R.id.textViewMovie);
        EditText editText = findViewById(R.id.editTextMovie);

        // Подписка на LiveData
        mainViewModel.getFavoriteMovie().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });

        // Сохранение
        findViewById(R.id.buttonSaveMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewModel.setText(new Movie(2, editText.getText().toString()));
            }
        });

        // Получение
        findViewById(R.id.buttonGetMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewModel.getText();
            }
        });
    }
}