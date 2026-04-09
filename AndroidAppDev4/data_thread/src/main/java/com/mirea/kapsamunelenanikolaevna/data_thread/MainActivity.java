package com.mirea.kapsamunelenanikolaevna.data_thread;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mirea.kapsamunelenanikolaevna.data_thread.databinding.ActivityMainBinding;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textView = binding.textViewRun;

        //отправляем runnable на немедленную обработку
        final Runnable runn1 = new Runnable() {
            @Override
            public void run() {
                textView.setText("runn1");
            }
        };

        final Runnable runn2 = new Runnable() {
            @Override
            public void run() {
                textView.setText("runn2");
            }
        };

        //указываем задержку выполнения
        final Runnable runn3 = new Runnable() {
            @Override
            public void run() {
                textView.setText("runn3");
            }
        };

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    runOnUiThread(runn1);
                    TimeUnit.SECONDS.sleep(2);
                    textView.postDelayed(runn3, 2000);
                    textView.post(runn2);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}

//runOnUiThread (немедленное выполнение в UI потоке).
// Если вызван из UI потока — выполняется сразу.
// Если вызван из фонового потока — ставится в очередь на выполнение

//post - немедленная постановка в очередь в UI потоке.
//Ставит задачу в конец очереди главного потока
//Выполняется как можно скорее, но после всех уже запланированных задач

//postDelayed - отложенное выполнение.
//Ставит задачу в очередь UI потока с задержкой в миллисекундах
//Задача выполнится не раньше, чем через указанное время