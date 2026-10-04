package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network;


import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models.CelebrityDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MockNetworkApi implements NetworkApi {

    private final List<CelebrityDto> celebrities = Arrays.asList(
            new CelebrityDto(1, "Тимоти Шаламе",
                    "",
                    "Американский актёр. Известен по фильмам «Назови меня своим именем», «Дюна», «Оппенгеймер»."),
            new CelebrityDto(2, "Зендея",
                    "",
                    "Американская актриса и певица. Известна по сериалу «Эйфория» и фильмам «Дюна», «Человек-паук»."),
            new CelebrityDto(3, "Том Холланд",
                    "",
                    "Британский актёр. Исполнитель роли Человека-паука в кинематографической вселенной Marvel."),
            new CelebrityDto(4, "Марго Робби",
                    "",
                    "Австралийская актриса. Известна по фильмам «Волк с Уолл-стрит», «Барби», «Однажды в Голливуде»."),
            new CelebrityDto(5, "Киллиан Мёрфи",
                    "",
                    "Ирландский актёр. Известен по роли Томаса Шелби в «Острых козырьках» и роли Оппенгеймера."),
            new CelebrityDto(6, "Флоренс Пью",
                    "",
                    "Британская актриса. Известна по фильмам «Маленькие женщины», «Не смотрите вверх», «Don't Worry Darling».")
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
