package com.mirea.kapsamunelenanikolaevna.celebritybase.domain.usecase.auth;

import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.AuthRepository;

public class RegisterUseCase {
    private final AuthRepository authRepository;

    public RegisterUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute(String email, String password, AuthRepository.AuthCallback callback) {
        authRepository.register(email, password, callback);
    }
}