package com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo;

import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.User;

public interface AuthRepository {
    void login(String email, String password, AuthCallback callback);
    void register(String email, String password, AuthCallback callback);
    void logout();
    User getCurrentUser();

    interface AuthCallback {
        void onSuccess(User user);
        void onError(String message);
    }
}