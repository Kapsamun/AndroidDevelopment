package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.User;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.AuthRepository;

public class AuthRepositoryImpl implements AuthRepository {

    private final FirebaseAuth firebaseAuth;

    public AuthRepositoryImpl() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(String email, String password, AuthCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(mapToDomain(firebaseAuth.getCurrentUser()));
                    } else {
                        callback.onError(task.getException() != null
                                ? task.getException().getMessage()
                                : "Ошибка входа");
                    }
                });
    }

    @Override
    public void register(String email, String password, AuthCallback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess(mapToDomain(firebaseAuth.getCurrentUser()));
                    } else {
                        callback.onError(task.getException() != null
                                ? task.getException().getMessage()
                                : "Ошибка регистрации");
                    }
                });
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
    }

    @Override
    public User getCurrentUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user != null ? mapToDomain(user) : null;
    }

    private User mapToDomain(FirebaseUser firebaseUser) {
        if (firebaseUser == null) return null;
        return new User(firebaseUser.getUid(), firebaseUser.getEmail());
    }
}