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
import com.mirea.kapsamunelenanikolaevna.celebritybase.presantation.mock.CelebrityDataSource;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final CelebrityRepository celebrityRepository;
    private final CelebrityDao celebrityDao;
    private final NetworkApi networkApi;

    // 1. LiveData для обновления статуса Use Cases
    private final MutableLiveData<String> celebrityResultLiveData = new MutableLiveData<>();

    // 2. MediatorLiveData для контрольного задания (объединяет БД и Network)
    private final MediatorLiveData<String> mediatorCelebrityData = new MediatorLiveData<>();

    public MainViewModel(CelebrityRepository celebrityRepository, CelebrityDao celebrityDao, NetworkApi networkApi) {
        this.celebrityRepository = celebrityRepository;
        this.celebrityDao = celebrityDao;
        this.networkApi = networkApi;
        Log.d(MainViewModel.class.getSimpleName(), "MainViewModel created");

        // Запускаем сборку данных в MediatorLiveData
        loadCombinedData();
    }

    public LiveData<String> getCelebrityResult() {
        return celebrityResultLiveData;
    }

    public LiveData<String> getMediatorCelebrityData() {
        return mediatorCelebrityData;
    }

    // Сохранение через UseCase (с 4 аргументами вашей доменной модели)
    public void saveCelebrity(String name) {
        if (name == null || name.trim().isEmpty()) {
            celebrityResultLiveData.setValue("Имя не может быть пустым!");
            return;
        }

        Celebrity celebrity = new Celebrity(
                (int) (System.currentTimeMillis() % 100000),
                name,
                "https://example.com/photo.png",
                "Известная личность"
        );

        new Thread(() -> {
            boolean result = new AddCelebrityToFavoriteUseCase(celebrityRepository).execute(celebrity);
            celebrityResultLiveData.postValue(result ? "Успешно сохранено: " + name : "Ошибка сохранения");
        }).start();
    }

    // Загрузка через UseCase (список List<Celebrity>)
    public void loadCelebrity() {
        new Thread(() -> {
            List<Celebrity> list = new GetFavoriteCelebritiesUseCase(celebrityRepository).execute();
            if (list != null && !list.isEmpty()) {
                StringBuilder builder = new StringBuilder("Сохраненные знаменитости:\n");
                for (Celebrity c : list) {
                    builder.append("• ").append(c.getName()).append("\n");
                }
                celebrityResultLiveData.postValue(builder.toString().trim());
            } else {
                celebrityResultLiveData.postValue("Список сохраненных пуст!");
            }
        }).start();
    }

    // Реализация требования №3: MediatorLiveData объединяет Room (БД) и MockNetworkApi (Сеть)
    public void loadCombinedData() {
        // Источник 1: Замоканная БД Room
        MutableLiveData<String> dbSource = new MutableLiveData<>();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            new Thread(() -> {
                List<CelebrityEntity> localList = celebrityDao.getAll();
                if (localList != null && !localList.isEmpty()) {
                    dbSource.postValue("Кеш БД: " + localList.get(0).name);
                } else {
                    dbSource.postValue("Кеш БД: записей нет");
                }
            }).start();
        }, 300);

        // Источник 2: Замоканная Сеть (NetworkApi)
        MutableLiveData<String> netSource = new MutableLiveData<>();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            new Thread(() -> {
                List<CelebrityDto> remoteList = networkApi.getCelebrities();
                if (remoteList != null && !remoteList.isEmpty()) {
                    netSource.postValue("Сеть (Mock API): " + remoteList.get(0).name);
                } else {
                    netSource.postValue("Сеть: ответ пуст");
                }
            }).start();
        }, 1200);

        // Подключаем источники к MediatorLiveData
        mediatorCelebrityData.addSource(dbSource, data -> {
            mediatorCelebrityData.setValue("Получено из источника -> " + data);
        });

        mediatorCelebrityData.addSource(netSource, data -> {
            mediatorCelebrityData.setValue("Получено из источника -> " + data);
        });
    }

    @Override
    protected void onCleared() {
        Log.d(MainViewModel.class.getSimpleName(), "MainViewModel cleared");
        super.onCleared();
    }
}