package com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo;

public interface ClientRepository {
    void saveClientInfo(String email, String name, boolean isGuest);
    String getClientEmail();
    String getClientName();
    boolean isGuest();
    void clear();
}