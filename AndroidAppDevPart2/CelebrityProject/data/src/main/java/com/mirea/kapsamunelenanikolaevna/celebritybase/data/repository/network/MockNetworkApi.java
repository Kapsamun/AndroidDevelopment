package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network;

import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models.CelebrityDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MockNetworkApi implements NetworkApi {

    // Картинки с CDN TMDB (работают без API-ключа)
    private final List<CelebrityDto> celebrities = Arrays.asList(
            new CelebrityDto(1, "Тимоти Шаламе",
                    "https://avatars.mds.yandex.net/i?id=b556cfda6d9d7ba8000b1b0e542c2d7b_l-9873353-images-thumbs&n=13",
                    "Американский актёр. Известен по фильмам «Дюна», «Оппенгеймер», «Зов предков»."),
            new CelebrityDto(2, "Зендея",
                    "https://assets.vogue.com/photos/6a68a0f84e8db1ed5e9e3b50/16:9/w_1280,c_limit/PR_02_0132_V1_FINAL_300DPI.jpg",
                    "Американская актриса. Сериал «Эйфория», фильмы «Дюна», «Человек-паук»."),
            new CelebrityDto(3, "Том Холланд",
                    "https://variety.com/wp-content/uploads/2026/07/P11894MV.jpg?crop=0px%2C29px%2C2000px%2C1125px&amp;resize=1000%2C563",
                    "Британский актёр. Роль Человека-паука в киновселенной Marvel."),
            new CelebrityDto(4, "Марго Робби",
                    "https://image.tmdb.org/t/p/w500/euDPyqLnuwaWMHajcU3oZ9uZezR.jpg",
                    "Австралийская актриса. «Барби», «Волк с Уолл-стрит», «Я, Тоня»."),
            new CelebrityDto(5, "Киллиан Мёрфи",
                    "https://avatars.mds.yandex.net/get-kinopoisk-image/1900788/90a98ddf-5d0e-4dbc-be99-79e9220704c1/3840x",
                    "Ирландский актёр. «Острые козырьки», «Оппенгеймер», «Начало».")
    );

    @Override
    public List<CelebrityDto> getCelebrities() {
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        return new ArrayList<>(celebrities);
    }

    @Override
    public List<CelebrityDto> searchCelebrities(String query) {
        if (query == null || query.trim().isEmpty()) return getCelebrities();
        String lower = query.toLowerCase().trim();
        return celebrities.stream()
                .filter(c -> c.name.toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    @Override
    public CelebrityDto getCelebrityById(int id) {
        for (CelebrityDto c : celebrities) {
            if (c.id == id) return c;
        }
        return null;
    }
}