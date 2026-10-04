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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.kapsamunelenanikolaevna.celebritybase.R;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.CelebrityRepoImpl;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.ClientRepositoryImpl;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.ClientRepository;
import com.mirea.kapsamunelenanikolaevna.celebritybase.presantation.practice4.CelebrityAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    private CelebrityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация ViewModel через собственную фабрику
        viewModel = new ViewModelProvider(this, new ViewModelFactory(this)).get(MainViewModel.class);

        // Инициализация RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCelebrities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CelebrityAdapter();
        recyclerView.setAdapter(adapter);

        EditText editTextCelebrity = findViewById(R.id.editTextCelebrity);
        Button buttonGet = findViewById(R.id.buttonGetCelebrity);
        Button buttonSave = findViewById(R.id.buttonSaveCelebrity);
        Button buttonFetchCombined = findViewById(R.id.buttonFetchCombined);



        viewModel.getMediatorCelebrityData().observe(this, list -> {
            if (list != null && !list.isEmpty()) {
                adapter.setCelebrities(list);
                Toast.makeText(MainActivity.this,
                        "Объединенный список отображен! Элементов: " + list.size(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getCelebritiesLiveData().observe(this, list -> {
            adapter.setCelebrities(list);
        });

        buttonSave.setOnClickListener(v -> {
            String name = editTextCelebrity.getText().toString().trim();
            if (!name.isEmpty()) {
                viewModel.saveCelebrity(name);
                editTextCelebrity.setText(""); // очищаем поле ввода
            }
        });

        buttonGet.setOnClickListener(v -> {
            viewModel.loadCelebrity();
        });

        buttonFetchCombined.setOnClickListener(v -> {
            viewModel.loadCombinedData();
        });

        viewModel.getCelebrityResult().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}