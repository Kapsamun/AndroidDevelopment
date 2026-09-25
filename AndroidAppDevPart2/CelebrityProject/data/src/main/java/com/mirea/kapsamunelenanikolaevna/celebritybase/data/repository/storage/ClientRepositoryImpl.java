package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage;

import android.content.Context;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.ClientRepository;

import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.sharedprefs.SharedPrefClientStorage;

public class ClientRepositoryImpl implements ClientRepository {

    private final ClientStorage storage;

    public ClientRepositoryImpl(Context context) {
        this.storage = new SharedPrefClientStorage(context);
    }

    @Override
    public void saveClientInfo(String email, String name, boolean isGuest) {
        storage.saveClientEmail(email);
        storage.saveClientName(name);
        storage.setGuest(isGuest);
    }

    @Override
    public String getClientEmail() {
        return storage.getClientEmail();
    }

    @Override
    public String getClientName() {
        return storage.getClientName();
    }

    @Override
    public boolean isGuest() {
        return storage.isGuest();
    }

    @Override
    public void clear() {
        storage.clear();
    }
}