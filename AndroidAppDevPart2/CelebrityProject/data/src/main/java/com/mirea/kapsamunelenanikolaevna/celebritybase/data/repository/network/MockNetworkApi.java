package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network;


import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models.CelebrityDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MockNetworkApi implements NetworkApi {

    private final List<CelebrityDto> celebrities = Arrays.asList(
            new CelebrityDto(1, "Тимоти Шаламе",
                    "https://i.pravatar.cc/300?img=11",
                    "Американский актёр. Известен по фильмам «Дюна», «Оппенгеймер»."),
            new CelebrityDto(2, "Зендея",
                    "https://i.pravatar.cc/300?img=5",
                    "Американская актриса. Известна по сериалу «Эйфория» и фильму «Дюна»."),
            new CelebrityDto(3, "Том Холланд",
                    "https://i.pravatar.cc/300?img=12",
                    "Британский актёр. Роль Человека-паука в кинематографической вселенной Marvel."),
            new CelebrityDto(4, "Марго Робби",
                    "https://i.pravatar.cc/300?img=9",
                    "Австралийская актриса. «Барби», «Волк с Уолл-стрит»."),
            new CelebrityDto(5, "Киллиан Мёрфи",
                    "https://i.pravatar.cc/300?img=15",
                    "Ирландский актёр. «Острые козырьки», «Оппенгеймер»."),
            new CelebrityDto(6, "Флоренс Пью",
                    "https://i.pravatar.cc/300?img=20",
                    "Британская актриса. «Маленькие женщины», «Не смотрите вверх».")
    );

    @Override
    public List<CelebrityDto> getCelebrities() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException ignored) {}
        return new ArrayList<>(celebrities);
    }

    @Override
    public List<CelebrityDto> searchCelebrities(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getCelebrities();
        }
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
