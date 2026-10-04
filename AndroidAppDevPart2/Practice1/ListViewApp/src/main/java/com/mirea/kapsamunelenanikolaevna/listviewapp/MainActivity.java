package com.mirea.kapsamunelenanikolaevna.listviewapp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private final String[] books = {
            "Лев Толстой — Война и мир",
            "Фёдор Достоевский — Преступление и наказание",
            "Михаил Булгаков — Мастер и Маргарита",
            "Александр Пушкин — Евгений Онегин",
            "Антон Чехов — Вишнёвый сад",
            "Иван Тургенев — Отцы и дети",
            "Николай Гоголь — Мёртвые души",
            "Максим Горький — На дне",
            "Александр Солженицын — Архипелаг ГУЛАГ",
            "Борис Пастернак — Доктор Живаго",
            "Владимир Набоков — Лолита",
            "Иван Гончаров — Обломов",
            "Михаил Лермонтов — Герой нашего времени",
            "Александр Грибоедов — Горе от ума",
            "Николай Островский — Как закалялась сталь",
            "Аркадий и Борис Стругацкие — Пикник на обочине",
            "Виктор Пелевин — Generation «П»",
            "Джордж Оруэлл — 1984",
            "Олдос Хаксли — О дивный новый мир",
            "Рэй Брэдбери — 451 градус по Фаренгейту",
            "Эрих Мария Ремарк — Три товарища",
            "Эрнест Хемингуэй — Старик и море",
            "Фрэнсис Скотт Фицджеральд — Великий Гэтсби",
            "Гарпер Ли — Убить пересмешника",
            "Джейн Остин — Гордость и предубеждение",
            "Чарльз Диккенс — Большие надежды",
            "Габriel García Márquez — Сто лет одиночества",
            "Харуки Мураками — Норвежский лес",
            "Умберто Эко — Имя розы",
            "Патрик Зюскинд — Парфюмер",
            "Стивен Кинг — Оно",
            "Дж. Р. Р. Толкин — Властелин колец",
            "Джордж Р. Р. Мартин — Игра престолов",
            "Агата Кристи — Убийство в Восточном экспрессе",
            "Артур Конан Дойл — Собака Баскервилей"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.books_list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                books
        ) {
            /// создает и заполняет каждый элемент
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);

                // Разделяем "Автор — Книга"
                String item = getItem(position);
                String[] parts = item.split(" — ", 2);

                text1.setText((position + 1) + ". " + parts[0]); // номер + автор
                text2.setText(parts.length > 1 ? parts[1] : "");  // название книги

                return view;
            }
        };

        listView.setAdapter(adapter);
    }
}
///Предназначен
/// для отображения небольших списков с одинаковыми элементами. Каждый элемент
/// списка представляет собой отдельный элемент интерфейса, который может быть
/// любым UI-компонентом (например, текст, изображение, кнопки).