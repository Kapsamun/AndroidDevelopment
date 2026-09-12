package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository;


import android.content.Context;


import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.MockNetworkApi;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.NetworkApi;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models.CelebrityDto;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.ClientStorage;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.room.AppDatabase;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.room.CelebrityDao;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.room.CelebrityEntity;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.sharedprefs.SharedPrefClientStorage;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.CelebrityRepository;

import java.util.ArrayList;
import java.util.List;

public class CelebrityRepoImpl implements CelebrityRepository {

    private final NetworkApi networkApi;
    private final CelebrityDao celebrityDao;
    private final ClientStorage clientStorage;

    public CelebrityRepoImpl(Context context) {
        this.networkApi = new MockNetworkApi();
        this.celebrityDao = AppDatabase.getInstance(context).celebrityDao();
        this.clientStorage = new SharedPrefClientStorage(context);
    }

    // ========== NetworkApi ==========
    @Override
    public List<Celebrity> getCelebrities() {
        List<CelebrityDto> dtoList = networkApi.getCelebrities();
        return mapListToDomain(dtoList);
    }

    @Override
    public List<Celebrity> searchCelebrities(String query) {
        List<CelebrityDto> dtoList = networkApi.searchCelebrities(query);
        return mapListToDomain(dtoList);
    }

    @Override
    public Celebrity getCelebrityById(int id) {
        CelebrityDto dto = networkApi.getCelebrityById(id);
        return dto != null ? mapToDomain(dto) : null;
    }

    // ========== Room (избранное) ==========
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

    // ========== Заглушка для распознавания ==========
    @Override
    public String recognizeCelebrityByPhoto(String imagePath) {
        return "Распознавание пока не реализовано";
    }

    // ========== SharedPreferences (клиент) ==========
    public void saveClientInfo(String email, String name, boolean isGuest) {
        clientStorage.saveClientEmail(email);
        clientStorage.saveClientName(name);
        clientStorage.setGuest(isGuest);
    }

    public String getClientEmail() {
        return clientStorage.getClientEmail();
    }

    public String getClientName() {
        return clientStorage.getClientName();
    }

    public boolean isGuest() {
        return clientStorage.isGuest();
    }

    public void clearClientInfo() {
        clientStorage.clear();
    }

    // ========== Мапперы ==========
    private Celebrity mapToDomain(CelebrityDto dto) {
        return new Celebrity(dto.id, dto.name, dto.photoUrl, dto.biography);
    }

    private List<Celebrity> mapListToDomain(List<CelebrityDto> dtoList) {
        List<Celebrity> result = new ArrayList<>();
        for (CelebrityDto dto : dtoList) {
            result.add(mapToDomain(dto));
        }
        return result;
    }
}
