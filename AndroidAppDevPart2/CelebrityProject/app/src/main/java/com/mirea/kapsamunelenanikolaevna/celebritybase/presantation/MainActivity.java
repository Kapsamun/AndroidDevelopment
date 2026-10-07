package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mirea.kapsamunelenanikolaevna.celebritybase.presantation.practice4.CelebrityAdapter;
import com.mirea.kapsamunelenanikolaevna.celebritybase.presantation.ViewModelFactory;
import com.mirea.kapsamunelenanikolaevna.celebritybase.presantation.MainViewModel;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.kapsamunelenanikolaevna.celebritybase.R;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private CelebrityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(this)).get(MainViewModel.class);


        RecyclerView recyclerView = findViewById(R.id.recyclerViewCelebrities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new com.mirea.kapsamunelenanikolaevna.celebritybase.presantation.practice4.CelebrityAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(celebrity -> {
            android.content.Intent intent = new android.content.Intent(
                    MainActivity.this,
                    CelebrityDetailActivity.class
            );
            intent.putExtra(CelebrityDetailActivity.EXTRA_ID, celebrity.getId());
            intent.putExtra(CelebrityDetailActivity.EXTRA_NAME, celebrity.getName());
            intent.putExtra(CelebrityDetailActivity.EXTRA_PHOTO, celebrity.getPhotoUrl());
            intent.putExtra(CelebrityDetailActivity.EXTRA_BIO, celebrity.getBiography());
            startActivity(intent);
        });

        EditText editTextCelebrity = findViewById(R.id.editTextCelebrity);
        Button buttonGet = findViewById(R.id.buttonGetCelebrity);
        Button buttonSave = findViewById(R.id.buttonSaveCelebrity);
        Button buttonFetchCombined = findViewById(R.id.buttonFetchCombined);

        // Список из репозитория (Retrofit / mock)
        viewModel.getCelebritiesLiveData().observe(this, list -> {
            if (list != null) {
                adapter.setItems(list);
            }
        });

        // Объединённый список (БД + сеть) — тоже List, не String
        viewModel.getMediatorCelebrityData().observe(this, list -> {
            if (list != null && !list.isEmpty()) {
                adapter.setItems(list);
                Toast.makeText(this,
                        "Объединено: " + list.size() + " элементов",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Текстовые сообщения
        viewModel.getError().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getCelebrityResult().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });

        buttonSave.setOnClickListener(v -> {
            String name = editTextCelebrity.getText().toString().trim();
            if (!name.isEmpty()) {
                viewModel.saveCelebrity(name);
                editTextCelebrity.setText("");
            } else {
                Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();
            }
        });

        buttonGet.setOnClickListener(v -> viewModel.loadCelebrity());

        buttonFetchCombined.setOnClickListener(v -> viewModel.loadCombinedData());
    }
}