package com.mirea.kapsamunelenanikolaevna.celebritybase.domain.usecase;

import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.CelebrityRepository;

public class GetCelebritiesByIdUseCase {
    private final CelebrityRepository repository;

    public GetCelebritiesByIdUseCase(CelebrityRepository repository) {
        this.repository = repository;
    }

    public Celebrity execute(int id) {
        return repository.getCelebrityById(id);
    }
}