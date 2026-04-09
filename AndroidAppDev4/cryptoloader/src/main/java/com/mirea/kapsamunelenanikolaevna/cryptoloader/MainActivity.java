package com.mirea.kapsamunelenanikolaevna.cryptoloader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import com.google.android.material.snackbar.Snackbar;
import com.mirea.kapsamunelenanikolaevna.cryptoloader.databinding.ActivityMainBinding;
import javax.crypto.SecretKey;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    public final String TAG = this.getClass().getSimpleName();
    private final int LoaderID = 1234;
    private ActivityMainBinding binding;
    private EditText editTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editTextInput = binding.editTextInput;
    }

    public void onClickButton(View view) {
        String inputText = editTextInput.getText().toString();

        // Проверка, что поле не пустое
        if (inputText.isEmpty()) {
            Snackbar.make(view, "Введите текст для шифрования!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        try {
            // 1. Генерируем ключ AES
            SecretKey secretKey = AESUtils.generateKey();

            // 2. Шифруем введенный текст
            byte[] encryptedText = AESUtils.encryptMsg(inputText, secretKey);

            // 3. Упаковываем зашифрованный текст и ключ в Bundle
            Bundle bundle = new Bundle();
            bundle.putByteArray(MyLoader.ARG_WORD, encryptedText);
            bundle.putByteArray("key", secretKey.getEncoded());

            // 4. Запускаем Loader для дешифровки
            LoaderManager.getInstance(this).initLoader(LoaderID, bundle, this);

            Log.d(TAG, "Текст зашифрован и отправлен в Loader");
            Snackbar.make(view, "Текст зашифрован, начинается дешифровка...", Snackbar.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e(TAG, "Ошибка шифрования: " + e.getMessage());
            Snackbar.make(view, "Ошибка шифрования!", Snackbar.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        if (i == LoaderID) {
            Toast.makeText(this, "onCreateLoader: " + i, Toast.LENGTH_SHORT).show();
            return new MyLoader(this, bundle);
        }
        throw new IllegalArgumentException("Invalid loader id");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        if (loader.getId() == LoaderID) {
            Log.d(TAG, "onLoadFinished: " + s);
            // Выводим результат через Toast и SnackBar
            Toast.makeText(this, "Расшифрованная фраза: " + s, Toast.LENGTH_LONG).show();
            Snackbar.make(binding.getRoot(), "Расшифровано: " + s, Snackbar.LENGTH_LONG).show();
        }
    }
 //Очищает данные, когда Loader уничтожается
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        Log.d(TAG, "onLoaderReset");
    }
}