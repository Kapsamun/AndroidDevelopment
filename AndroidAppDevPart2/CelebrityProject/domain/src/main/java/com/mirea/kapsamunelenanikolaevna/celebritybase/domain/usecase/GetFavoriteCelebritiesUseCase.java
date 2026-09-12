package com.mirea.kapsamunelenanikolaevna.celebritybase.domain.usecase;

import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.CelebrityRepository;

import java.util.List;

public class GetFavoriteCelebritiesUseCase {
    private final CelebrityRepository repository;

    public GetFavoriteCelebritiesUseCase(CelebrityRepository repository) {
        this.repository = repository;
    }

    public List<Celebrity> execute() {
        return repository.getFavorites();
    }
}
