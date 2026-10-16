package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network;

import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models.TmdbPersonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbApi {

    // Популярные актёры
    @GET("person/popular")
    Call<TmdbPersonResponse> getPopularPeople(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    // Поиск по имени
    @GET("search/person")
    Call<TmdbPersonResponse> searchPeople(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("language") String language
    );
}