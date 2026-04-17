package com.mirea.kapsamunelenanikolaevna.androidappdev6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mirea.kapsamunelenanikolaevna.androidappdev6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Инициализация SharedPreferences
        prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        loadData();

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void loadData() {
        String group = prefs.getString("GROUP", "unknown");
        int number = prefs.getInt("NUMBER", 0);
        String movie = prefs.getString("MOVIE", "");

        binding.etGroup.setText(group);
        binding.etNumber.setText(number != 0 ? String.valueOf(number) : "");
        binding.etMovie.setText(movie);
    }

    private void saveData() {
        String group = binding.etGroup.getText().toString();
        String numberStr = binding.etNumber.getText().toString();
        String movie = binding.etMovie.getText().toString();

        int number = 0;
        if (!numberStr.isEmpty()) {
            number = Integer.parseInt(numberStr);
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("GROUP", group);
        editor.putInt("NUMBER", number);
        editor.putString("MOVIE", movie);
        editor.apply();

        Toast.makeText(MainActivity.this, "Данные сохранены!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}

//SharedPreferences — нужен для хранения простых данных в виде пар «ключ-значение».
