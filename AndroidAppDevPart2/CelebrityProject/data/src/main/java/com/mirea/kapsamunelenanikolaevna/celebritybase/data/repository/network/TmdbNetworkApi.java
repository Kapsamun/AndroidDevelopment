package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network;

import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models.CelebrityDto;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models.TmdbPersonDto;
import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models.TmdbPersonResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TmdbNetworkApi implements NetworkApi {

    // ← ВСТАВЬ СВОЙ КЛЮЧ
    private static final String API_KEY = "9c845e1ffe8f90aab4a425c29afba68b";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String IMAGE_BASE = "https://image.tmdb.org/t/p/w500";

    private final TmdbApi api;

    public TmdbNetworkApi() {
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(log)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(TmdbApi.class);
    }

    @Override
    public List<CelebrityDto> getCelebrities() {
        try {
            TmdbPersonResponse response = api
                    .getPopularPeople(API_KEY, "ru-RU", 1)
                    .execute()
                    .body();
            return map(response);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<CelebrityDto> searchCelebrities(String query) {
        try {
            TmdbPersonResponse response = api
                    .searchPeople(API_KEY, query, "ru-RU")
                    .execute()
                    .body();
            return map(response);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public CelebrityDto getCelebrityById(int id) {
        // Для простоты — ищем в уже загруженном списке
        for (CelebrityDto c : getCelebrities()) {
            if (c.id == id) return c;
        }
        return null;
    }

    private List<CelebrityDto> map(TmdbPersonResponse response) {
        List<CelebrityDto> result = new ArrayList<>();
        if (response == null || response.results == null) return result;

        for (TmdbPersonDto p : response.results) {
            String photo = (p.profilePath != null && !p.profilePath.isEmpty())
                    ? IMAGE_BASE + p.profilePath
                    : "";
            String bio = p.department != null
                    ? "Известен(на) в: " + p.department
                    : "Актёр / актриса";
            result.add(new CelebrityDto(p.id, p.name, photo, bio));
        }
        return result;
    }
}
