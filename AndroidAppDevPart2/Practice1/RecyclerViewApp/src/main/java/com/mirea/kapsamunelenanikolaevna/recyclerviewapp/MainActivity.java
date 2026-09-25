package com.mirea.kapsamunelenanikolaevna.recyclerviewapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<HistoricalEvent> events = getEvents();
        EventAdapter adapter = new EventAdapter(events);
        recyclerView.setAdapter(adapter);
    }

    private List<HistoricalEvent> getEvents() {
        List<HistoricalEvent> list = new ArrayList<>();

        list.add(new HistoricalEvent(
                "988 год — Крещение Руси",
                "Князь Владимир принял христианство и сделал его государственной религией Киевской Руси.",
                "russia_map"));

        list.add(new HistoricalEvent(
                "1240 год — Невская битва",
                "Александр Невский разбил шведские войска на реке Неве.",
                "flag"));

        list.add(new HistoricalEvent(
                "1380 год — Куликовская битва",
                "Дмитрий Донской одержал победу над войском Мамая.",
                ""));

        list.add(new HistoricalEvent(
                "1480 год — Стояние на Угре",
                "Окончательное освобождение Руси от ордынского ига.",
                "russia_map"));

        list.add(new HistoricalEvent(
                "1613 год — Начало династии Романовых",
                "Земский собор избрал царём Михаила Фёдоровича Романова.",
                "flag"));

        list.add(new HistoricalEvent(
                "1703 год — Основание Санкт-Петербурга",
                "Пётр I заложил новую столицу Российской империи.",
                ""));

        list.add(new HistoricalEvent(
                "1812 год — Отечественная война",
                "Разгром армии Наполеона и изгнание французов из России.",
                "russia_map"));

        list.add(new HistoricalEvent(
                "1861 год — Отмена крепостного права",
                "Александр II подписал манифест об освобождении крестьян.",
                "flag"));

        list.add(new HistoricalEvent(
                "1917 год — Октябрьская революция",
                "Большевики пришли к власти, началась советская эпоха.",
                ""));

        list.add(new HistoricalEvent(
                "1945 год — Победа в Великой Отечественной войне",
                "9 мая — День Победы над нацистской Германией.",
                ""));

        list.add(new HistoricalEvent(
                "1961 год — Полёт Гагарина",
                "Юрий Гагарин стал первым человеком в космосе.",
                ""));

        list.add(new HistoricalEvent(
                "1991 год — Распад СССР",
                "Образование независимой Российской Федерации.",
                ""));

        return list;

    }
}