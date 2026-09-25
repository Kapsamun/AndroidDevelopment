package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network;

import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models.CelebrityDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CelebrityApi {

    @GET("celebrities")
    Call<List<CelebrityDto>> getCelebrities();

    @GET("celebrities")
    Call<List<CelebrityDto>> searchCelebrities(@Query("q") String query);

    @GET("celebrities/{id}")
    Call<CelebrityDto> getCelebrityById(@Path("id") int id);
}