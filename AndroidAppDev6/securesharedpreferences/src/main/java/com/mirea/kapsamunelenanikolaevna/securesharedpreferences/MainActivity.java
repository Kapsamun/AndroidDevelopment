package com.mirea.kapsamunelenanikolaevna.securesharedpreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.mirea.kapsamunelenanikolaevna.securesharedpreferences.databinding.ActivityMainBinding;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private android.content.SharedPreferences secureSharedPreferences;
    private String masterKeyAlias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createMasterKey();

        initEncryptedPreferences(); //хранилище

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        binding.btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    //хранится в AndroidKeyStore
    private void createMasterKey() {
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка создания ключа", Toast.LENGTH_LONG).show();
        }
    }

    private void initEncryptedPreferences() {
        try {
            secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias, //пвсевдоним ключа
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка инициализации", Toast.LENGTH_LONG).show();
        }
    }

    private void saveData() {
        String actorName = binding.etActorName.getText().toString().trim();

        if (actorName.isEmpty()) {
            Toast.makeText(this, "Введите имя актёра", Toast.LENGTH_SHORT).show();
            return;
        }

        android.content.SharedPreferences.Editor editor = secureSharedPreferences.edit();
        editor.putString("FAVORITE_ACTOR", actorName);
        editor.apply();

        Toast.makeText(this, "Данные зашифрованы и сохранены!", Toast.LENGTH_SHORT).show();

        // Очищаем поле ввода
        binding.etActorName.setText("");
    }

    private void loadData() {
        String actorName = secureSharedPreferences.getString("FAVORITE_ACTOR", "не указан");

        binding.tvActorName.setText("Любимый актёр: " + actorName);

        Toast.makeText(this, "Данные расшифрованы!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
