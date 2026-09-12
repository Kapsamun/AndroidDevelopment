package com.mirea.kapsamunelenanikolaevna.celebritybase.domain.usecase;

import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.CelebrityRepository;

public class RemoveCelebrityFromFavoriteUseCase {
    private final CelebrityRepository repository;

    public RemoveCelebrityFromFavoriteUseCase(CelebrityRepository repository) {
        this.repository = repository;
    }

    public boolean execute(int celebrityId) {
        return repository.removeFromFavorites(celebrityId);
    }
}
