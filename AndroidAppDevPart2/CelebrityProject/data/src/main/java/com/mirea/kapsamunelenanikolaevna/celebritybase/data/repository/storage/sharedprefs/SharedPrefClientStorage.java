package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage.ClientStorage;

public class SharedPrefClientStorage implements ClientStorage {

    private static final String PREFS_NAME = "client_prefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_GUEST = "is_guest";

    private final SharedPreferences prefs;

    public SharedPrefClientStorage(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void saveClientEmail(String email) {
        prefs.edit().putString(KEY_EMAIL, email).apply();
    }

    @Override
    public String getClientEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    @Override
    public void saveClientName(String name) {
        prefs.edit().putString(KEY_NAME, name).apply();
    }

    @Override
    public String getClientName() {
        return prefs.getString(KEY_NAME, "Гость");
    }

    @Override
    public void setGuest(boolean isGuest) {
        prefs.edit().putBoolean(KEY_GUEST, isGuest).apply();
    }

    @Override
    public boolean isGuest() {
        return prefs.getBoolean(KEY_GUEST, true);
    }

    @Override
    public void clear() {
        prefs.edit().clear().apply();
    }
}