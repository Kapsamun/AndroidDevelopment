package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.storage;

public interface ClientStorage {
    void saveClientEmail(String email);
    String getClientEmail();
    void saveClientName(String name);
    String getClientName();
    void setGuest(boolean isGuest);
    boolean isGuest();
    void clear();
}
