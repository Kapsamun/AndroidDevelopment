package com.mirea.kapsamunelenanikolaevna.celebritybase.domain.usecase;

import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.models.Celebrity;
import com.mirea.kapsamunelenanikolaevna.celebritybase.domain.repo.CelebrityRepository;
import java.util.List;

public class SearchCelebritiesUseCase {
    private final CelebrityRepository repository;

    public SearchCelebritiesUseCase(CelebrityRepository repository) {
        this.repository = repository;
    }

    public List<Celebrity> execute(String query) {
        return repository.searchCelebrities(query);
    }
}