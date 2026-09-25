package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository;

import android.content.Context;
import android.util.Log;

import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.CelebrityApi;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.MockNetworkApi;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.NetworkApi;

import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.RetrofitClient;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models.CelebrityDto;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.room.AppDatabase;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.room.CelebrityDao;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.room.CelebrityEntity;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.CelebrityRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class CelebrityRepoImpl implements CelebrityRepository {

    private final NetworkApi mockApi;          // заглушка
    private final CelebrityApi retrofitApi;    // Retrofit
    private final CelebrityDao celebrityDao;

    private static final String TAG = "CelebrityRepoImpl";

    public CelebrityRepoImpl(Context context) {
        this.mockApi = new MockNetworkApi();
        this.retrofitApi = RetrofitClient.getApi();
        this.celebrityDao = AppDatabase.getInstance(context).celebrityDao();
    }

    @Override
    public List<Celebrity> getCelebrities() {
        // 1. Пробуем получить данные через Retrofit
        try {
            Response<List<CelebrityDto>> response = retrofitApi.getCelebrities().execute();
            if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                Log.d(TAG, "Данные получены через Retrofit");
                return mapListToDomain(response.body());
            }
        } catch (Exception e) {
            Log.w(TAG, "Retrofit недоступен, используем заглушку: " + e.getMessage());
        }

        // 2. Fallback — заглушка (MockNetworkApi) — это и есть требование контрольного задания
        Log.d(TAG, "Используется заглушка MockNetworkApi");
        return mapListToDomain(mockApi.getCelebrities());
    }

    @Override
    public List<Celebrity> searchCelebrities(String query) {
        try {
            Response<List<CelebrityDto>> response = retrofitApi.searchCelebrities(query).execute();
            if (response.isSuccessful() && response.body() != null) {
                return mapListToDomain(response.body());
            }
        } catch (Exception ignored) {}

        return mapListToDomain(mockApi.searchCelebrities(query));
    }

    @Override
    public Celebrity getCelebrityById(int id) {
        try {
            Response<CelebrityDto> response = retrofitApi.getCelebrityById(id).execute();
            if (response.isSuccessful() && response.body() != null) {
                return mapToDomain(response.body());
            }
        } catch (Exception ignored) {}

        CelebrityDto dto = mockApi.getCelebrityById(id);
        return dto != null ? mapToDomain(dto) : null;
    }

    @Override
    public boolean addToFavorites(Celebrity celebrity) {
        if (celebrity == null) return false;
        celebrityDao.insert(new CelebrityEntity(
                celebrity.getId(),
                celebrity.getName(),
                celebrity.getPhotoUrl(),
                celebrity.getBiography()
        ));
        return true;
    }

    @Override
    public boolean removeFromFavorites(int celebrityId) {
        celebrityDao.deleteById(celebrityId);
        return true;
    }

    @Override
    public List<Celebrity> getFavorites() {
        List<CelebrityEntity> entities = celebrityDao.getAll();
        List<Celebrity> result = new ArrayList<>();
        for (CelebrityEntity e : entities) {
            result.add(new Celebrity(e.id, e.name, e.photoUrl, e.biography));
        }
        return result;
    }

    @Override
    public String recognizeCelebrityByPhoto(String imagePath) {
        return "Распознавание пока не реализовано";
    }

    private Celebrity mapToDomain(CelebrityDto dto) {
        return new Celebrity(dto.id, dto.name, dto.photoUrl, dto.biography);
    }

    private List<Celebrity> mapListToDomain(List<CelebrityDto> dtoList) {
        List<Celebrity> result = new ArrayList<>();
        if (dtoList == null) return result;
        for (CelebrityDto dto : dtoList) {
            result.add(mapToDomain(dto));
        }
        return result;
    }
}