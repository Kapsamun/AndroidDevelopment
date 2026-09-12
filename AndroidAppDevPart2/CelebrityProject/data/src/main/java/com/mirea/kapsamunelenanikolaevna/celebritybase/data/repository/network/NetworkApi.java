package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network;

import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models.CelebrityDto;

import java.util.List;

public interface NetworkApi {
    List<CelebrityDto> getCelebrities();
    List<CelebrityDto> searchCelebrities(String query);
    CelebrityDto getCelebrityById(int id);
}