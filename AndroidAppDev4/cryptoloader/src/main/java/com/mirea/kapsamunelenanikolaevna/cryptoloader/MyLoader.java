package com.mirea.kapsamunelenanikolaevna.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;
import javax.crypto.SecretKey;

public class MyLoader extends AsyncTaskLoader<String> {

    private String decryptedText;
    public static final String ARG_WORD = "word";
    private byte[] encryptedText;
    private byte[] keyBytes;

    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null) {
            // Получаем зашифрованный текст и ключ из Bundle
            encryptedText = args.getByteArray(ARG_WORD);
            keyBytes = args.getByteArray("key");

            Log.d("MyLoader", "Получены данные: шифротекст длиной " +
                    (encryptedText != null ? encryptedText.length : 0) +
                    " байт, ключ длиной " + (keyBytes != null ? keyBytes.length : 0) + " байт");
        }
    }

    //Вызывается системой автоматически после создания Loader'а.
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d("MyLoader", "onStartLoading");
        forceLoad(); // Запускаем загрузку
    }

    @Override
    public String loadInBackground() {
        Log.d("MyLoader", "loadInBackground - начинаем дешифровку");

        try {
            // Проверка наличия данных
            if (encryptedText == null || keyBytes == null) {
                Log.e("MyLoader", "Нет данных для дешифровки");
                return "Ошибка: нет данных для дешифровки";
            }

            // Эмуляция длительной операции (как в вашем коде)
            SystemClock.sleep(5000);

            // Восстанавливаем ключ из байтов
            SecretKey secretKey = AESUtils.restoreKey(keyBytes);

            // Дешифруем текст
            decryptedText = AESUtils.decryptMsg(encryptedText, secretKey);

            Log.d("MyLoader", "Дешифровка успешно завершена: " + decryptedText);
            return decryptedText; //Результат возвращается в onLoadFinished()

        } catch (Exception e) {
            Log.e("MyLoader", "Ошибка дешифровки: " + e.getMessage());
            e.printStackTrace();
            return "Ошибка дешифровки: " + e.getMessage();
        }
    }

    //Вызывается  перед отправкой результата.
    // Доставляет результат из фонового потока в UI поток.

    @Override
    public void deliverResult(String data) {
        super.deliverResult(data);
        Log.d("MyLoader", "deliverResult: " + data);
    }
}