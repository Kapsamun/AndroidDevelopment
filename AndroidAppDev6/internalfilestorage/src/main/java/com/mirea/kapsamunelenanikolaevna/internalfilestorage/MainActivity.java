package com.mirea.kapsamunelenanikolaevna.internalfilestorage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private String fileName = "mirea.txt";
    private EditText etHistoricalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etHistoricalDate = findViewById(R.id.etHistoricalDate);

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            String string = etHistoricalDate.getText().toString();
            if (!string.isEmpty()) {
                writeFile(string);
            } else {
                Toast.makeText(MainActivity.this, "Введите данные", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Метод для записи файла
    private void writeFile(String data) {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
            Toast.makeText(this, "Данные сохранены!", Toast.LENGTH_SHORT).show();
            etHistoricalDate.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Метод для чтения файла
    public String getTextFromFile() {
        FileInputStream fin = null;
        try {
            fin = openFileInput(fileName);
            byte[] bytes = new byte[fin.available()];  //узнаём размер файла в байтах
            fin.read(bytes);
            String text = new String(bytes);
            return text;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (fin != null)
                    fin.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}