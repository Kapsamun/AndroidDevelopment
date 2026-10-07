package com.mirea.kapsamunelenanikolaevna.celebritybase.presantation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.NetworkApi;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models.CelebrityDto;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.room.CelebrityDao;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.room.CelebrityEntity;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.CelebrityRepository;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.usecase.AddCelebrityToFavoriteUseCase;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.usecase.GetFavoriteCelebritiesUseCase;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private final CelebrityRepository celebrityRepository;
    private final CelebrityDao celebrityDao;
    private final NetworkApi networkApi;

    private final MutableLiveData<String> celebrityResultLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Celebrity>> celebritiesLiveData = new MutableLiveData<>();

    // Постоянные источники LiveData
    private final MutableLiveData<List<Celebrity>> dbSource = new MutableLiveData<>();
    private final MutableLiveData<List<Celebrity>> netSource = new MutableLiveData<>();

    //  Списки для хранения последних данных
    private final List<Celebrity> currentDbList = new ArrayList<>();
    private final List<Celebrity> currentNetList = new ArrayList<>();

    // Единый MediatorLiveData
    private final MediatorLiveData<List<Celebrity>> mediatorCelebrityData = new MediatorLiveData<>();

    public MainViewModel(CelebrityRepository celebrityRepository, CelebrityDao celebrityDao, NetworkApi networkApi) {
        this.celebrityRepository = celebrityRepository;
        this.celebrityDao = celebrityDao;
        this.networkApi = networkApi;
        Log.d(MainViewModel.class.getSimpleName(), "MainViewModel created");

        // Подключаем источники
        setupMediatorSources();

        // Загрузка при создании
        loadCombinedData();
        loadCelebrity();
    }

    private void setupMediatorSources() {
        mediatorCelebrityData.addSource(dbSource, data -> {
            currentDbList.clear();
            if (data != null) {
                currentDbList.addAll(data);
            }
            emitMergedCelebrities();
        });

        mediatorCelebrityData.addSource(netSource, data -> {
            currentNetList.clear();
            if (data != null) {
                currentNetList.addAll(data);
            }
            emitMergedCelebrities();
        });
    }

    public LiveData<String> getCelebrityResult() {
        return celebrityResultLiveData;
    }

    public LiveData<List<Celebrity>> getMediatorCelebrityData() {
        return mediatorCelebrityData;
    }

    public LiveData<List<Celebrity>> getCelebritiesLiveData() {
        return celebritiesLiveData;
    }

    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void saveCelebrity(String name) {
        if (name == null || name.trim().isEmpty()) {
            celebrityResultLiveData.setValue("Имя не может быть пустым!");
            return;
        }

        Celebrity celebrity = new Celebrity(
                (int) (System.currentTimeMillis() % 100000),
                name,
                "",
                "Известная личность"
        );

        new Thread(() -> {
            boolean result = new AddCelebrityToFavoriteUseCase(celebrityRepository).execute(celebrity);
            celebrityResultLiveData.postValue(result ? "Успешно сохранено: " + name : "Ошибка сохранения");
        }).start();
    }

    public void loadCelebrity(){
            new Thread(() -> {
                try {
                    List<Celebrity> list = celebrityRepository.getCelebrities();
                    if (list == null || list.isEmpty()) {
                        errorLiveData.postValue("Список пуст или сервер не ответил");
                        celebritiesLiveData.postValue(new java.util.ArrayList<>());
                    } else {
                        errorLiveData.postValue(null); // ошибок нет
                        celebritiesLiveData.postValue(list);
                    }
                } catch (Exception e) {
                    errorLiveData.postValue("Ошибка сети: " + e.getMessage());
                    celebritiesLiveData.postValue(new java.util.ArrayList<>());
                }
            }).start();
        }

    // Метод поставляет новые данные в dbSource и netSource
    public void loadCombinedData() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            new Thread(() -> {
                List<CelebrityEntity> localList = celebrityDao.getAll();
                List<Celebrity> convertedList = new ArrayList<>();
                if (localList != null) {
                    for (CelebrityEntity entity : localList) {
                        convertedList.add(new Celebrity(
                                entity.id,
                                entity.name,
                                entity.photoUrl,
                                entity.biography
                        ));
                    }
                }
                dbSource.postValue(convertedList);
            }).start();
        }, 300);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            new Thread(() -> {
                List<CelebrityDto> remoteList = networkApi.getCelebrities();
                List<Celebrity> convertedList = new ArrayList<>();
                if (remoteList != null) {
                    for (CelebrityDto dto : remoteList) {
                        convertedList.add(new Celebrity(
                                dto.id,
                                dto.name,
                                dto.photoUrl,
                                dto.biography
                        ));
                    }
                }
                netSource.postValue(convertedList);
            }).start();
        }, 1200);
    }

    private void emitMergedCelebrities() {
        List<Celebrity> combined = new ArrayList<>(currentDbList);

        for (Celebrity remoteItem : currentNetList) {
            boolean exists = false;
            for (Celebrity localItem : currentDbList) {
                if (localItem.getName().equalsIgnoreCase(remoteItem.getName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                combined.add(remoteItem);
            }
        }

        mediatorCelebrityData.setValue(combined);
    }

    @Override
    protected void onCleared() {
        Log.d(MainViewModel.class.getSimpleName(), "MainViewModel cleared");
        super.onCleared();
    }
}