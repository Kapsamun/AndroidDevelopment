package com.mirea.kapsamunelenanikolaevna.celebritybase.domain.usecase.auth;

import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.AuthRepository;

public class LogoutUseCase {
    private final AuthRepository authRepository;

    public LogoutUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public void execute() {
        authRepository.logout();
    }
}