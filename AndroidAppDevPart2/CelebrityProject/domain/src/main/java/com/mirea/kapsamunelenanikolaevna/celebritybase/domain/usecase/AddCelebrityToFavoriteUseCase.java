package com.mirea.kapsamunelenanikolaevna.celebritybase.domain.usecase;

import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.CelebrityRepository;

public class AddCelebrityToFavoriteUseCase {
    private final CelebrityRepository repository;

    public AddCelebrityToFavoriteUseCase(CelebrityRepository repository) {
        this.repository = repository;
    }

    public boolean execute(Celebrity celebrity) {
        return repository.addToFavorites(celebrity);
    }
}
