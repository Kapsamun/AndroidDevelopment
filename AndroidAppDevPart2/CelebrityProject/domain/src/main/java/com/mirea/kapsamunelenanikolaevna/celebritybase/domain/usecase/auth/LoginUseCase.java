package com.mirea.kapsamunelenanikolaevna.celebritybase.domain.usecase.auth;

import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.AuthRepository;

public class LoginUseCase {
    private final AuthRepository authRepository;

    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String email, String password, AuthRepository.AuthCallback callback) {
        authRepository.login(email, password, callback);
    }
}