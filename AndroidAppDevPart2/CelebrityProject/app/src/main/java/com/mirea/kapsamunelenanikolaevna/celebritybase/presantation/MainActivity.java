package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mirea.kapsamunelenanikolaevna.celebritybase.R;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.CelebrityRepoImpl;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.ClientRepositoryImpl;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.ClientRepository;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация ViewModel через собственную фабрику
        viewModel = new ViewModelProvider(this, new ViewModelFactory(this)).get(MainViewModel.class);

        TextView textViewCelebrity = findViewById(R.id.textViewCelebrity);
        EditText editTextCelebrity = findViewById(R.id.editTextCelebrity);
        Button buttonGet = findViewById(R.id.buttonGetCelebrity);
        Button buttonSave = findViewById(R.id.buttonSaveCelebrity);

        TextView textViewMediator = findViewById(R.id.textViewMediator);
        Button buttonFetchCombined = findViewById(R.id.buttonFetchCombined);

        // Подписка на LiveData выполнения Use Cases
        viewModel.getCelebrityResult().observe(this, textViewCelebrity::setText);

        // Подписка на MediatorLiveData (Контрольное задание: объединение БД + Сеть)
        viewModel.getMediatorCelebrityData().observe(this, textViewMediator::setText);

        // Обработка кликов
        buttonSave.setOnClickListener(v -> {
            String name = editTextCelebrity.getText().toString().trim();
            viewModel.saveCelebrity(name);
        });

        buttonGet.setOnClickListener(v -> {
            viewModel.loadCelebrity();
        });

        buttonFetchCombined.setOnClickListener(v -> {
            viewModel.loadCombinedData();
        });
    }
}