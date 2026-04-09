package com.mirea.kapsamunelenanikolaevna.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;
import com.mirea.kapsamunelenanikolaevna.thread.databinding.ActivityMainBinding;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextView infoTextView;


    private Handler mainHandler;
    private int counter = 0;

    private TextView resultTextView;
    private EditText editTextTotalPairs;
    private EditText editTextStudyDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater()); //создаем Java-представление всех элементов
        setContentView(binding.getRoot());

        // Инициализация элементов через binding
        infoTextView = binding.textViewThread;
        resultTextView = binding.textViewResult;
        editTextTotalPairs = binding.editTextTotalPairs;
        editTextStudyDays = binding.editTextStudyDays;

        mainHandler = new Handler(Looper.getMainLooper()); // Handler механизм для отправки сообщений между потоками.  позволяет из фонового потока отправлять задачи на выполнение в UI поток для обновления интерфейса
        Thread mainThread = Thread.currentThread();

        infoTextView.setText("Имя текущего потока: " + mainThread.getName() + "\n");
        //меняем имя и выводим в текстовое поле
        mainThread.setName("МОЙ НОМЕР ГРУППЫ: 9, НОМЕР ПО СПИСКУ: 9, МОЙ ЛЮБИМЫЙ ФИЛЬМ: Иллюзия обмана");
        infoTextView.append("\n Новое имя потока: " + mainThread.getName());

        Log.i(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));
        Log.i(MainActivity.class.getSimpleName(), "Group: " + mainThread.getThreadGroup());


    }

    public void onButtonInfo(View view) {
        //создаем новый объект потока
        new Thread(new Runnable() {
            @Override
            public void run() {
                int numberThread = counter++;
                Log.i(MainActivity.class.getSimpleName(),
                        String.format("Запущен поток № %d студентом группы № %s номер по списку № %d ", numberThread, "БСБО-09-23", 9));

                long endTime = System.currentTimeMillis() + 20 * 1000;

                while (System.currentTimeMillis() < endTime) {
                    synchronized (this) {
                        try {
                            wait(endTime - System.currentTimeMillis());
                            Log.i(MainActivity.class.getSimpleName(), "End Time: " + endTime);
                        } catch (Exception exception) {
                            throw new RuntimeException(exception);
                        }
                    }
                    Log.i("Thread Project", "Выполнен поток № " + numberThread);
                }
            }
        }).start();
    }

        public void onCalculateClick (View view){
            // Получаем введенные данные
            String totalPairsStr = editTextTotalPairs.getText().toString();
            String studyDaysStr = editTextStudyDays.getText().toString();

            // Проверка на пустые поля
            if (totalPairsStr.isEmpty() || studyDaysStr.isEmpty()) {
                resultTextView.setText("Пожалуйста, заполните оба поля!");
                return;
            }

            int totalPairs = Integer.parseInt(totalPairsStr);
            int studyDays = Integer.parseInt(studyDaysStr);

            if (studyDays == 0) {
                resultTextView.setText("Количество учебных дней не может быть равно 0!");
                return;
            }

            // Запускаем фоновый поток для расчета
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int numberThread = counter++;
                    Log.i(MainActivity.class.getSimpleName(),
                            String.format("Запущен поток расчета № %d студентом группы № %s номер по списку № %d",
                                    numberThread, "БСБО-09-23", 9));

                    // Вычисляем среднее количество пар в день
                    double averagePairs = (double) totalPairs / studyDays;

                    // Форматируем результат с двумя знаками после запятой
                    String result = String.format("Среднее количество пар в день за месяц: %.2f", averagePairs);

                    // Возвращаем результат в UI поток
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            resultTextView.setText(result);
                            Log.i("Thread Project", "Выполнен поток расчета № " + numberThread +
                                    ", Результат: " + averagePairs);

                            // Добавляем информацию в infoTextView о расчете
                            infoTextView.append("\n\nПоток расчета №" + numberThread + " вычислил: " + result);
                        }
                    });
                }
            }).start();
        }
    }