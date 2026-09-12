package com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network;


import com.mirea.kapsamunelenanikolaevna.celebritybase.data.repository.network.models.CelebrityDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MockNetworkApi implements NetworkApi {

    private final List<CelebrityDto> celebrities = Arrays.asList(
            new CelebrityDto(1, "Тимоти Шаламе",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5c/Timoth%C3%A9e_Chalamet_2019.jpg/440px-Timoth%C3%A9e_Chalamet_2019.jpg",
                    "Американский актёр. Известен по фильмам «Назови меня своим именем», «Дюна», «Оппенгеймер»."),
            new CelebrityDto(2, "Зендея",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/Zendaya_-_2019_by_Glenn_Francis.jpg/440px-Zendaya_-_2019_by_Glenn_Francis.jpg",
                    "Американская актриса и певица. Известна по сериалу «Эйфория» и фильмам «Дюна», «Человек-паук»."),
            new CelebrityDto(3, "Том Холланд",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/3/3e/Tom_Holland_by_Gage_Skidmore.jpg/440px-Tom_Holland_by_Gage_Skidmore.jpg",
                    "Британский актёр. Исполнитель роли Человека-паука в кинематографической вселенной Marvel."),
            new CelebrityDto(4, "Марго Робби",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/2/29/Margot_Robbie_in_2019_%28cropped%29.jpg/440px-Margot_Robbie_in_2019_%28cropped%29.jpg",
                    "Австралийская актриса. Известна по фильмам «Волк с Уолл-стрит», «Барби», «Однажды в Голливуде»."),
            new CelebrityDto(5, "Киллиан Мёрфи",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Cillian_Murphy_at_Berlinale_2024.jpg/440px-Cillian_Murphy_at_Berlinale_2024.jpg",
                    "Ирландский актёр. Известен по роли Томаса Шелби в «Острых козырьках» и роли Оппенгеймера."),
            new CelebrityDto(6, "Флоренс Пью",
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Florence_Pugh_2022.jpg/440px-Florence_Pugh_2022.jpg",
                    "Британская актриса. Известна по фильмам «Маленькие женщины», «Не смотрите вверх», «Don't Worry Darling».")
    );

    @Override
    public List<CelebrityDto> getCelebrities() {
        // Имитация задержки сети (по желанию можно убрать)
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
