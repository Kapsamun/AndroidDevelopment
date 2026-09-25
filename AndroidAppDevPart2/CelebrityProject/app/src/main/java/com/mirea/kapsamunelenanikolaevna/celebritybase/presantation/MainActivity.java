package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mirea.kapsamunelenanikolaevna.celebritybase.R;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private CelebrityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ViewModel
        viewModel = new ViewModelProvider(this, new ViewModelFactory(this))
                .get(MainViewModel.class);

        // RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCelebrities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CelebrityAdapter();
        recyclerView.setAdapter(adapter);

        // Клик по элементу
        adapter.setOnItemClickListener(celebrity -> {
            Toast.makeText(this, celebrity.getName(), Toast.LENGTH_SHORT).show();
        });

        // Подписка на LiveData списка (контрольное задание)
        viewModel.getCelebrities().observe(this, celebrities -> {
            adapter.setItems(celebrities);
        });

        // Старые элементы UI
        TextView textViewCelebrity = findViewById(R.id.textViewCelebrity);
        EditText editTextCelebrity = findViewById(R.id.editTextCelebrity);
        Button buttonGet = findViewById(R.id.buttonGetCelebrity);
        Button buttonSave = findViewById(R.id.buttonSaveCelebrity);
        TextView textViewMediator = findViewById(R.id.textViewMediator);
        Button buttonFetchCombined = findViewById(R.id.buttonFetchCombined);

        viewModel.getCelebrityResult().observe(this, textViewCelebrity::setText);
        viewModel.getMediatorCelebrityData().observe(this, textViewMediator::setText);

        buttonSave.setOnClickListener(v -> {
            String name = editTextCelebrity.getText().toString().trim();
            viewModel.saveCelebrity(name);
        });

        buttonGet.setOnClickListener(v -> viewModel.loadCelebrity());

        buttonFetchCombined.setOnClickListener(v -> viewModel.loadCombinedData());
    }
}