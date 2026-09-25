package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation.mock;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class CelebrityDataSource {

    // Имитация получения данных из локальной базы данных
    public LiveData<String> getDbData() {
        MutableLiveData<String> liveData = new MutableLiveData<>();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            liveData.setValue("Локальная БД: Киану Ривз (кеш)");
        }, 500); // имитация задержки чтения из базы
        return liveData;
    }

    // Имитация получения данных из сетевого API
    public LiveData<String> getNetworkData() {
        MutableLiveData<String> liveData = new MutableLiveData<>();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            liveData.setValue("Сетевой сервис: Леонардо Ди Каприо (актуально)");
        }, 1500); // сетевой запрос выполняется чуть дольше
        return liveData;
    }
}
