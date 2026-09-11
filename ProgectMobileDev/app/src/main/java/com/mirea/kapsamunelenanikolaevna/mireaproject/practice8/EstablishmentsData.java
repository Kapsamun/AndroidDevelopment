package com.mirea.kapsamunelenanikolaevna.mireaproject.practice8;

import com.mirea.kapsamunelenanikolaevna.mireaproject.practice8.Establishment;
import java.util.ArrayList;
import java.util.List;

public class EstablishmentsData {

    public static List<Establishment> getEstablishments() {
        List<Establishment> establishments = new ArrayList<>();

        // Заведение 1: Garda
        establishments.add(new Establishment(
                "1",
                "Garda",
                "Ресторан",
                "Москва, ул. Петровка 17с1",
                "+7 (499) 877-45-67",
                "09:00 - 00:00",
                4.9,
                "Итальянский ресторан с авторской кухней. Уютная атмосфера, отличный сервис и вкуснейшие блюда от шеф-повара.",
                55.764480, 37.616467
        ));

        // Заведение 2: Coffeeshop Company
        establishments.add(new Establishment(
                "2",
                "Coffeeshop Company",
                "Кафе",
                "Москва, ул. Тверская, 15",
                "+7 (495) 123-45-67",
                "08:00 - 22:00",
                4.6,
                "Популярная сеть кофеен с отличным кофе из разных стран мира. Большой выбор десертов и выпечки.",
                55.760000, 37.600000
        ));

        // Заведение 3: Pivbar
        establishments.add(new Establishment(
                "3",
                "Pivbar",
                "Бар",
                "Москва, ул. Мясницкая, 24",
                "+7 (495) 222-33-44",
                "12:00 - 02:00",
                4.4,
                "Большой выбор крафтового пива и закусок. Живая музыка по выходным. Отличное место для встреч с друзьями.",
                55.761000, 37.619000
        ));

        // Заведение 4: Киноцентр «Октябрь»
        establishments.add(new Establishment(
                "4",
                "Киноцентр «Октябрь»",
                "Кинотеатр",
                "Москва, ул. Новый Арбат, 24",
                "+7 (495) 691-67-67",
                "09:00 - 03:00",
                4.7,
                "Современный кинотеатр с 15 залами, включая IMAX. Регулярные премьеры и фестивали.",
                55.754000, 37.582000
        ));

        return establishments;
    }
}